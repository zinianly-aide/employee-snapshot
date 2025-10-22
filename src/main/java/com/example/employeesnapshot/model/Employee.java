package com.example.employeesnapshot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    private Long id;
    
    @NotBlank(message = "员工ID不能为空")
    @Size(max = 50, message = "员工ID长度不能超过50个字符")
    private String employeeId;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String name;
    
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String departmentName;
    
    @NotBlank(message = "职位不能为空")
    @Size(max = 100, message = "职位长度不能超过100个字符")
    private String position;
    
    @Size(max = 100, message = "工位长度不能超过100个字符")
    private String workstation;
    
    @NotBlank(message = "状态不能为空")
    @Size(max = 50, message = "状态长度不能超过50个字符")
    private String status;
    
    private Date lastUpdateTime;
    private Date effectiveDate;
    private String changeType;
}