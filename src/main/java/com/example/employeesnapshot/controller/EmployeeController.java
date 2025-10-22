package com.example.employeesnapshot.controller;

import com.example.employeesnapshot.dto.ApiResponse;
import com.example.employeesnapshot.model.Employee;
import com.example.employeesnapshot.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 同步员工数据
    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<String>> syncEmployeeData(@Valid @RequestBody @NotEmpty(message = "员工数据不能为空") List<Employee> employeeData) {
        log.info("开始同步员工数据，员工数量: {}", employeeData.size());
        employeeService.syncEmployeeData(employeeData);
        return ResponseEntity.ok(ApiResponse.success("员工数据同步成功"));
    }

    // 获取指定日期的员工快照
    @GetMapping("/snapshot")
    public ResponseEntity<ApiResponse<List<Employee>>> getEmployeeSnapshot(
            @RequestParam @NotNull(message = "日期不能为空") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.info("获取员工快照，日期: {}", date);
        List<Employee> snapshot = employeeService.getEmployeeSnapshotByDate(date);
        return ResponseEntity.ok(ApiResponse.success("获取员工快照成功", snapshot));
    }

    // 获取指定日期的在职员工数
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getEmployeeCount(
            @RequestParam @NotNull(message = "日期不能为空") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.info("获取在职员工数量，日期: {}", date);
        int count = employeeService.getActiveEmployeeCount(date);
        return ResponseEntity.ok(ApiResponse.success("获取员工数量成功", count));
    }

    // 获取员工数同比变化率
    @GetMapping("/growth/year-over-year")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getYearOverYearGrowth(
            @RequestParam @NotNull(message = "日期不能为空") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.info("获取员工同比变化率，日期: {}", date);
        Map<String, Double> growth = employeeService.getYearOverYearGrowthRate(date);
        return ResponseEntity.ok(ApiResponse.success("获取同比变化率成功", growth));
    }

    // 获取员工数环比变化率
    @GetMapping("/growth/month-over-month")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getMonthOverMonthGrowth(
            @RequestParam @NotNull(message = "日期不能为空") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.info("获取员工环比变化率，日期: {}", date);
        Map<String, Double> growth = employeeService.getMonthOverMonthGrowthRate(date);
        return ResponseEntity.ok(ApiResponse.success("获取环比变化率成功", growth));
    }
}