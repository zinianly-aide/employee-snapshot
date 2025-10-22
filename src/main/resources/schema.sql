-- 创建员工表
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL UNIQUE COMMENT '员工ID',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    department_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    position VARCHAR(100) NOT NULL COMMENT '职位',
    workstation VARCHAR(100) COMMENT '工位',
    status VARCHAR(50) NOT NULL COMMENT '状态(ACTIVE,RESIGNED)',
    last_update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
    effective_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '生效日期',
    change_type VARCHAR(50) DEFAULT 'NEW' COMMENT '变更类型(NEW,UPDATE,RESIGNED)',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引优化
    INDEX idx_employee_id (employee_id),
    INDEX idx_status (status),
    INDEX idx_department_name (department_name),
    INDEX idx_position (position),
    INDEX idx_last_update_time (last_update_time),
    INDEX idx_effective_date (effective_date),
    INDEX idx_change_type (change_type),
    INDEX idx_status_department (status, department_name),
    INDEX idx_status_update_time (status, last_update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工信息表';

-- 创建员工历史记录表
CREATE TABLE IF NOT EXISTS employee_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL COMMENT '员工ID',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    department_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    position VARCHAR(100) NOT NULL COMMENT '职位',
    workstation VARCHAR(100) COMMENT '工位',
    status VARCHAR(50) NOT NULL COMMENT '状态',
    change_time TIMESTAMP NOT NULL COMMENT '变更时间',
    change_type VARCHAR(50) NOT NULL COMMENT '变更类型',
    
    -- 索引优化
    INDEX idx_employee_id (employee_id),
    INDEX idx_change_time (change_time),
    INDEX idx_change_type (change_type),
    INDEX idx_employee_change_time (employee_id, change_time),
    INDEX idx_change_type_time (change_type, change_time),
    INDEX idx_status_change_time (status, change_time),
    INDEX idx_department_change_time (department_name, change_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工历史记录表';