# 智能药店管理系统 (Pysystem)

一个基于SSM框架的智能药店管理系统，提供完整的药品管理、采购、销售、库存管理和统计分析功能。
<img width="2560" height="1346" alt="image" src="https://github.com/user-attachments/assets/97d1e39b-e0f5-4746-8f34-78b0128b9f6d" />

<img width="2533" height="1346" alt="image" src="https://github.com/user-attachments/assets/52c2d1fa-57e5-42d6-ac7c-fdad2e5241f0" />

<img width="2558" height="1346" alt="image" src="https://github.com/user-attachments/assets/31117f13-d939-42bc-b58b-887960d44184" />
<img width="2534" height="1346" alt="image" src="https://github.com/user-attachments/assets/e790447e-5761-4bef-b88b-5758a937d099" />


## 项目简介

智能药店管理系统是一个现代化的药店管理解决方案，采用B/S架构，支持多用户角色管理，提供直观的用户界面和强大的后台管理功能。

## 技术栈

### 后端技术
- **框架**: Spring 5.3.20 + SpringMVC + MyBatis 3.5.9
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **服务器**: Tomcat 9.0

### 前端技术
- **UI框架**: LayUI 2.9.8
- **图表库**: ECharts (用于数据可视化)
- **JavaScript**: jQuery 3.6.0
- **样式**: CSS3 + Flex/Grid布局

### 开发工具
- **IDE**: IntelliJ IDEA
- **版本控制**: Git
- **数据库管理**: MySQL Workbench

## 功能模块

### 1. 用户管理
- 用户登录/登出
- 角色权限管理（管理员/员工）
- 用户信息维护

### 2. 药品管理
- 药品信息管理（增删改查）
- 药品分类管理
- 药品图片上传
- 药品状态管理（上架/下架）

### 3. 采购管理
- 药品采购记录
- 供应商管理
- 采购统计

### 4. 销售管理
- 药品销售记录
- 销售统计
- 销售报表

### 5. 库存管理
- 库存查询
- 库存预警
- 库存盘点

### 6. 统计分析
- 销售数据分析
- 库存数据分析
- 图表可视化展示

## 系统特色

- **响应式设计**: 支持多种设备访问
- **数据安全**: 密码MD5加密存储
- **操作便捷**: 直观的用户界面设计
- **功能完善**: 覆盖药店管理全流程
- **性能优化**: 数据库查询优化，页面加载快速

## 快速开始

### 环境要求

- JDK 1.8+
- MySQL 8.0+
- Tomcat 9.0+
- Maven 3.6+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE pharmacy_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 执行初始化脚本：
```sql
USE pharmacy_system;
SOURCE src/main/resources/pharmacy_system.sql;
```

### 配置文件修改

修改 `src/main/resources/jdbc.properties` 中的数据库连接信息：
```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/pharmacy_system?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
jdbc.username=your_username
jdbc.password=your_password
```

### 项目构建

```bash
# 克隆项目
git clone https://github.com/Dream22180971/Pysystem.git

# 进入项目目录
cd pysystem

# 使用Maven构建项目
mvn clean package

# 将生成的war文件部署到Tomcat
cp target/pysystem.war $CATALINA_HOME/webapps/
```

### 启动应用

1. 启动MySQL数据库
2. 启动Tomcat服务器
3. 访问应用：`http://localhost:8080/pysystem`

### 默认账号

- **管理员账号**: admin / admin123
- **员工账号**: emp01 / employee123

## 项目结构

```
pysystem/
├── src/main/java/com/pharmacy/
│   ├── bean/           # 实体类
│   ├── controller/     # 控制器
│   ├── service/        # 服务接口
│   ├── service/impl/   # 服务实现
│   ├── mapper/         # MyBatis接口
│   ├── util/          # 工具类
│   └── vo/            # 视图对象
├── src/main/resources/
│   ├── mapper/         # MyBatis映射文件
│   ├── spring-mvc.xml  # Spring MVC配置
│   ├── spring-mybatis.xml # Spring+MyBatis配置
│   └── jdbc.properties # 数据库配置
├── src/main/webapp/
│   ├── pages/         # 前端页面
│   ├── static/        # 静态资源
│   └── WEB-INF/       # Web配置
└── pom.xml            # Maven配置
```

## 开发指南

### 代码规范

- 遵循Java编码规范
- 使用有意义的变量名和方法名
- 添加必要的注释
- 统一使用UTF-8编码

### 数据库设计

系统包含9个核心表：
- userinfo（用户表）
- drugs（药品表）
- category（分类表）
- part（角色表）
- purchase（采购表）
- sale（销售表）
- kcxx（库存表）
- repertory（仓库表）
- counter（柜台表）

### API接口

系统采用RESTful风格设计API接口，主要接口包括：
- `/login` - 用户登录
- `/logout` - 用户登出
- `/drugs/**` - 药品管理接口
- `/purchase/**` - 采购管理接口
- `/sale/**` - 销售管理接口
- `/statistic/**` - 统计分析接口

## 部署说明

### 生产环境部署

1. **数据库配置**：使用生产环境数据库，修改连接参数
2. **安全配置**：配置SSL证书，启用HTTPS
3. **性能优化**：配置数据库连接池，启用缓存
4. **监控配置**：配置日志监控和性能监控

### Docker部署（可选）

```dockerfile
FROM tomcat:9.0-jdk8
COPY target/pysystem.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

## 贡献指南

欢迎提交Issue和Pull Request来改进项目。

### 开发流程

1. Fork项目
2. 创建功能分支
3. 提交代码变更
4. 发起Pull Request

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 项目地址：https://github.com/Dream22180971/Pysystem
- Issue反馈：https://github.com/Dream22180971/Pysystem/issues

## 更新日志

### v1.0.0 (2026-04-11)
- 初始版本发布
- 实现基础药品管理功能
- 完成用户权限系统
- 添加统计分析模块
