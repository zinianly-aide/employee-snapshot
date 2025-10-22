package com.example.employeesnapshot.service.impl;

import com.example.employeesnapshot.mapper.EmployeeMapper;
import com.example.employeesnapshot.mapper.EmployeeHistoryMapper;
import com.example.employeesnapshot.model.Employee;
import com.example.employeesnapshot.model.EmployeeHistory;
import com.example.employeesnapshot.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private EmployeeHistoryMapper employeeHistoryMapper;

    @Override
    @Transactional
    public void syncEmployeeData(List<Employee> newEmployeeData) {
        if (newEmployeeData == null || newEmployeeData.isEmpty()) {
            throw new IllegalArgumentException("员工数据不能为空");
        }
        
        Date currentTime = new Date();
        
        // 获取当前所有在职员工
        List<Employee> currentActiveEmployees = employeeMapper.selectAllActive();
        
        // 创建新员工ID集合用于快速查找
        Set<String> newEmployeeIds = newEmployeeData.stream()
                .map(Employee::getEmployeeId)
                .collect(Collectors.toSet());
        
        // 处理新员工数据和更新现有员工
        for (Employee newEmployee : newEmployeeData) {
            newEmployee.setLastUpdateTime(currentTime);
            
            // 检查员工是否已存在
            Employee existingEmployee = employeeMapper.selectByEmployeeId(newEmployee.getEmployeeId());
            
            if (existingEmployee == null) {
                // 新员工
                newEmployee.setChangeType("NEW");
                newEmployee.setEffectiveDate(currentTime);
                employeeMapper.insert(newEmployee);
                
                // 记录历史
                saveHistory(newEmployee, "NEW");
                log.info("新增员工: {}", newEmployee.getEmployeeId());
            } else {
                // 检查是否有变化
                boolean hasChanges = checkForChanges(existingEmployee, newEmployee);
                
                if (hasChanges) {
                    // 更新员工信息
                    newEmployee.setChangeType("UPDATE");
                    newEmployee.setEffectiveDate(currentTime);
                    employeeMapper.update(newEmployee);
                    
                    // 记录历史
                    saveHistory(newEmployee, "UPDATE");
                    log.info("更新员工信息: {}", newEmployee.getEmployeeId());
                }
            }
        }
        
        // 检查离职员工（在上游数据中不存在但在本地激活表中存在）
        for (Employee currentEmployee : currentActiveEmployees) {
            if (!newEmployeeIds.contains(currentEmployee.getEmployeeId())) {
                // 员工离职
                currentEmployee.setStatus("RESIGNED");
                currentEmployee.setChangeType("RESIGNED");
                currentEmployee.setEffectiveDate(currentTime);
                currentEmployee.setLastUpdateTime(currentTime);
                
                employeeMapper.update(currentEmployee);
                saveHistory(currentEmployee, "RESIGNED");
                log.info("员工离职: {}", currentEmployee.getEmployeeId());
            }
        }
    }

    private boolean checkForChanges(Employee existing, Employee newData) {
        return !Objects.equals(existing.getDepartmentName(), newData.getDepartmentName()) ||
               !Objects.equals(existing.getPosition(), newData.getPosition()) ||
               !Objects.equals(existing.getWorkstation(), newData.getWorkstation()) ||
               !Objects.equals(existing.getStatus(), newData.getStatus()) ||
               !Objects.equals(existing.getName(), newData.getName());
    }

    private void saveHistory(Employee employee, String changeType) {
        // Create history record from employee data
        EmployeeHistory history = new EmployeeHistory();
        
        // Copy employee details
        history.setEmployeeId(employee.getEmployeeId());
        history.setName(employee.getName());
        history.setDepartmentName(employee.getDepartmentName());
        history.setPosition(employee.getPosition());
        history.setWorkstation(employee.getWorkstation());
        history.setStatus(employee.getStatus());
        
        // Set history-specific fields
        history.setChangeTime(new Date());
        history.setChangeType(changeType);
        
        // Save to history table
        employeeHistoryMapper.insert(history);
    }

    @Override
    public List<Employee> getEmployeeSnapshotByDate(Date snapshotDate) {
        return employeeMapper.selectSnapshotByDate(snapshotDate);
    }

    @Override
    public int getActiveEmployeeCount(Date date) {
        List<Employee> snapshot = getEmployeeSnapshotByDate(date);
        return (int) snapshot.stream().filter(e -> "ACTIVE".equals(e.getStatus())).count();
    }

    @Override
    public Map<String, Double> getYearOverYearGrowthRate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        // 当前日期员工数
        int currentCount = getActiveEmployeeCount(date);
        
        // 去年同期
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        int lastYearCount = getActiveEmployeeCount(lastYearDate);
        
        // 计算同比增长率
        double growthRate = lastYearCount == 0 ? 0 : 
                ((double) (currentCount - lastYearCount) / lastYearCount) * 100;
        
        Map<String, Double> result = new HashMap<>();
        result.put("growthRate", growthRate);
        return result;
    }

    @Override
    public Map<String, Double> getMonthOverMonthGrowthRate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        // 当前日期员工数
        int currentCount = getActiveEmployeeCount(date);
        
        // 上月同期
        calendar.add(Calendar.MONTH, -1);
        Date lastMonthDate = calendar.getTime();
        int lastMonthCount = getActiveEmployeeCount(lastMonthDate);
        
        // 计算环比增长率
        double growthRate = lastMonthCount == 0 ? 0 : 
                ((double) (currentCount - lastMonthCount) / lastMonthCount) * 100;
        
        Map<String, Double> result = new HashMap<>();
        result.put("growthRate", growthRate);
        return result;
    }
}