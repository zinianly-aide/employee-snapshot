package com.example.employeesnapshot.mapper;

import com.example.employeesnapshot.model.EmployeeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface EmployeeHistoryMapper {
    // 插入历史记录
    int insert(EmployeeHistory history);
    
    // 查询指定日期范围内的变更记录
    List<EmployeeHistory> selectByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    // 查询特定员工的历史变更记录
    List<EmployeeHistory> selectByEmployeeId(@Param("employeeId") String employeeId);
    
    // 删除所有历史记录（测试用）
    int deleteAll();
}