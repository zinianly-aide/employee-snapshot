package com.example.employeesnapshot.mapper;

import com.example.employeesnapshot.model.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    // 插入员工信息
    int insert(Employee employee);
    
    // 更新员工信息
    int update(Employee employee);
    
    // 根据员工ID查询
    Employee selectByEmployeeId(String employeeId);
    
    // 查询所有在职员工
    List<Employee> selectAllActive();
    
    // 根据日期查询在职员工快照
    List<Employee> selectSnapshotByDate(@Param("snapshotDate") Date snapshotDate);
    
    // 删除所有员工记录（测试用）
    int deleteAll();
}