# 员工快照查询系统

员工快照查询系统是一个集成员工信息管理与历史记录追踪的Spring Boot应用。该系统支持员工数据同步、历史记录保存、快照查询和统计分析等功能。

## 功能特性

- **员工数据同步**：支持批量同步员工数据，自动检测变更并记录历史
- **快照查询**：支持按日期查询员工状态快照，获取特定时间点的员工信息
- **历史记录**：自动记录员工信息的所有变更，包括新增、更新和离职
- **统计分析**：提供员工数统计、同比增长率和环比增长率等分析功能
- **数据验证**：完整的输入数据验证机制，确保数据完整性
- **异常处理**：统一的异常处理和API响应格式

## 技术栈

- **后端框架**：Spring Boot 3.2.0
- **ORM框架**：MyBatis
- **数据库**：MySQL
- **构建工具**：Maven
- **开发语言**：Java 17
- **数据校验**：Spring Validation
- **日志框架**：SLF4J + Logback

## 系统架构

- **控制器层**：提供REST API接口，处理HTTP请求
- **服务层**：实现核心业务逻辑，包括数据同步、变更检测和统计分析
- **数据访问层**：通过MyBatis与数据库交互
- **实体层**：定义员工和历史记录等数据模型
- **配置层**：管理系统配置和CORS等设置

## 项目结构

```
src/
├── main/
│   ├── java/com/example/employeesnapshot/
│   │   ├── controller/       # 控制器
│   │   ├── service/          # 服务接口
│   │   │   └── impl/         # 服务实现
│   │   ├── mapper/           # 数据访问接口
│   │   ├── model/            # 数据模型
│   │   ├── dto/              # 数据传输对象
│   │   ├── exception/        # 异常处理
│   │   ├── config/           # 配置类
│   │   └── EmployeeSnapshotApplication.java  # 应用入口
│   └── resources/
│       ├── mapper/           # MyBatis映射文件
│       ├── application.properties  # 应用配置
│       └── schema.sql        # 数据库表结构
└── test/                     # 单元测试和集成测试
```

## 数据库设计

- **employee表**：存储当前有效的员工信息
- **employee_history表**：存储员工信息的历史变更记录

两个表都有完善的索引优化，支持高效查询。

## API接口

### 1. 员工数据同步
- **URL**：`/api/employees/sync`
- **方法**：POST
- **请求体**：员工数据列表

### 2. 获取员工快照
- **URL**：`/api/employees/snapshot`
- **方法**：GET
- **参数**：date (yyyy-MM-dd)

### 3. 获取员工数量统计
- **URL**：`/api/employees/count`
- **方法**：GET
- **参数**：date (yyyy-MM-dd)

### 4. 获取同比增长率
- **URL**：`/api/employees/growth/year-over-year`
- **方法**：GET
- **参数**：date (yyyy-MM-dd)

### 5. 获取环比增长率
- **URL**：`/api/employees/growth/month-over-month`
- **方法**：GET
- **参数**：date (yyyy-MM-dd)

## 部署说明

1. **环境要求**：
   - Java 17+
   - MySQL 8.0+
   - Maven 3.6+

2. **数据库准备**：
   - 创建名为`employee_snapshot`的数据库
   - 执行`schema.sql`文件创建表结构

3. **配置修改**：
   - 修改`application.properties`中的数据库连接信息

4. **构建与运行**：
   ```bash
   mvn clean package
   java -jar target/employee-snapshot-0.0.1-SNAPSHOT.jar
   ```

## 安全说明

- 系统已配置CORS支持跨域访问
- 提供完整的数据验证机制
- 所有输入参数都经过严格校验

## 开发说明

- 使用Lombok减少样板代码
- 遵循RESTful API设计规范
- 统一的异常处理和日志记录
- 支持Docker容器化部署（通过docker-compose.yml）

## 许可证

[MIT License](https://opensource.org/licenses/MIT)
