package com.example.employeesnapshot.service;

import com.example.employeesnapshot.model.Employee;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    // 同步员工数据
    void syncEmployeeData(List<Employee> newEmployeeData);
    
    // 获取指定日期的员工快照
    List<Employee> getEmployeeSnapshotByDate(Date snapshotDate);
    
    // 获取在职员工数统计
    int getActiveEmployeeCount(Date date);
    
    // 获取员工数同比变化率
    Map<String, Double> getYearOverYearGrowthRate(Date date);
    
    // 获取员工数环比变化率
    Map<String, Double> getMonthOverMonthGrowthRate(Date date);
}