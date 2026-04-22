# 智能药店管理系统 (Pysystem)

**当前版本：v2.0（开发分支）**：新增本地知识库（Markdown）与“药智助手”（RAG + 可选阿里大模型）。

基于 **Spring Boot 3** 与 **Vue 3** 前后端分离的智能药店后台：员工、药品、分类、采购、销售、库存、统计与日志审计等模块，统一 REST API（`/api`）与 JWT 鉴权；支持 **RBAC**（管理员 / 员工）与分页列表服务端排序。

## 项目简介

系统采用 B/S 架构，支持 **管理员** 与 **员工** 两种业务角色（由 `userinfo.P_id` 与 JWT `role` 声明）；前端为单页应用（Vite），后端为内嵌 Tomcat 的 Spring Boot 应用，数据存储于 MySQL。

## 技术栈

### 后端

| 技术 | 说明 |
|------|------|
| Java | **17** |
| 框架 | **Spring Boot 3.4.x**（内嵌 Tomcat） |
| 安全 | **Spring Security 6** + **JWT**（Bearer Token） |
| 持久层 | **MyBatis** + XML（`src/main/resources/mapper/*.xml`） |
| 数据库 | **MySQL 8.x** |
| 构建 | **Maven** |

### 前端（`frontend/`）

| 技术 | 说明 |
|------|------|
| 框架 | **Vue 3** + **TypeScript** |
| 构建 | **Vite 8.x** |
| UI | **Element Plus** + 图标库 |
| 状态 / 路由 | **Pinia**、**Vue Router 5** |
| HTTP | **Axios**（请求头携带 JWT；支持从 Pinia / `localStorage` 解析 Token） |
| 图表 | **ECharts** + **vue-echarts** |

### 开发工具（建议）

- 后端：IntelliJ IDEA  
- 前端：VS Code / Cursor  
- 数据库：MySQL 客户端、Navicat 等  

## 功能模块

| 模块 | 说明 |
|------|------|
| 登录 | `POST /api/auth/login`，返回 JWT；可选 `expectedPId` 与账号角色一致校验 |
| 权限 | Spring Security：用户/审计仅管理员；销售/采购查询与业务写操作限管理员+员工；前端路由与菜单按角色收敛 |
| 智能看板 | 汇总 SKU、库存预警、本月销售、图表（销售/采购聚合） |
| 员工管理 | 用户 CRUD（密码 MD5；列表不返回密码字段） |
| 药品 / 分类 | 药品与分类维护 |
| 销售 / 采购 | 销售与采购记录 CRUD |
| 库存 | 库存维护；预警（数量 &lt; 60） |
| 智能统计 | 销售结构饼图、采购分布柱状图（数据来自 `sale` / `purchase` 表聚合） |
| 日志审计 | 读 `audit_log` 表（需执行 `audit_log.sql`） |
| 本地知识库 | 扫描项目 `kb/` 目录下的 Markdown，提供检索/读取接口（`/api/kb/**`） |
| 药智助手 | 内部流程/系统使用问答：知识库检索 +（可选）阿里模型生成（`/api/ai/chat`） |

## 环境要求

- **JDK 17**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Node.js 18+**（仅前端开发/构建）

## 快速开始

### 1. 数据库

创建库并导入脚本（在项目根目录 `pysystem` 下，路径按你本机调整）：

```bash
mysql -u root -p --default-character-set=utf8mb4 < src/main/resources/pharmacy_system.sql
mysql -u root -p --default-character-set=utf8mb4 < src/main/resources/audit_log.sql
```

Windows 也可使用仓库内脚本（会 **删除并重建** `pharmacy_system` 库，慎用）：

```powershell
.\scripts\reset_pharmacy_db.ps1
```

### 2. 后端配置

主要配置见 `src/main/resources/application.yml`：

- 数据源：默认 `jdbc:mysql://localhost:3306/pharmacy_system`，用户 `root`，**密码必须通过环境变量 `DB_PASSWORD` 配置**  
- JWT：密钥 `APP_JWT_SECRET`（生产环境务必修改，长度满足 HS256 要求）  
- CORS：`app.cors.allowed-origin-patterns` 默认允许 `http://localhost:*`  
- 知识库目录：`KB_PATH`（默认 `kb`）  
- 阿里模型（可选）：`ALI_AI_API_KEY`、`ALI_AI_MODEL`（默认 `qwen3.6-plus`）、`ALI_AI_ENDPOINT`

Windows（PowerShell）示例：

```powershell
$env:DB_PASSWORD="你的数据库密码"
$env:APP_JWT_SECRET="change-me-change-me-change-me-change-me"
# 可选：启用阿里模型
$env:ALI_AI_API_KEY="你的Key"
$env:ALI_AI_MODEL="qwen3.6-plus"
mvn -DskipTests spring-boot:run
```

启动：

```bash
cd pysystem
mvn -DskipTests spring-boot:run
```

默认端口：**8080**。健康检查：`GET http://localhost:8080/actuator/health`

打包：

```bash
mvn -DskipTests clean package
# 先结束占用 target 下 jar 的 Java 进程，避免 repackage 重命名失败
java -jar target/pysystem.jar
```

### 3. 前端

```bash
cd frontend
npm install
npm run dev
```

默认：**http://localhost:5173**  
开发环境下 `/api` 由 Vite 代理到 `http://localhost:8080`（见 `frontend/vite.config.ts`）。

生产构建：

```bash
cd frontend
npm run build
```

将 `frontend/dist` 交由 Nginx 等托管，并配置反向代理将 `/api` 指到后端地址。

### 默认测试账号

开发环境可使用种子数据中的测试账号（用户名/密码以 `src/main/resources/pharmacy_system.sql` 为准）。

## 项目结构（概要）

```
pysystem/
├── pom.xml
├── scripts/
│   └── reset_pharmacy_db.ps1      # 一键重建库并导入 SQL（慎用）
├── kb/                            # 本地知识库（Markdown）
├── frontend/                      # Vue3 前端工程
│   ├── src/
│   │   ├── api/                   # Axios 与各模块接口
│   │   ├── config/                # RBAC 路由与菜单角色配置
│   │   ├── pages/                 # 页面（看板、各业务、登录）
│   │   ├── layouts/               # 后台布局
│   │   ├── stores/                # Pinia（登录态）
│   │   └── router/
│   ├── vite.config.ts             # 开发代理 /api -> 8080
│   └── package.json
└── src/main/
    ├── java/com/pharmacy/
    │   ├── PharmacyApplication.java
    │   ├── bean/                  # 实体
    │   ├── controller/            # REST，前缀多为 /api/...
    │   ├── mapper/                # MyBatis 接口
    │   ├── service/
    │   ├── security/              # JWT、SecurityConfig、过滤器
    │   ├── util/
    │   └── vo/
    └── resources/
        ├── application.yml
        ├── mybatis-config.xml
        ├── mapper/                # MyBatis XML
        ├── pharmacy_system.sql    # 建表 + 种子数据
        └── audit_log.sql          # 审计表 + 演示数据
```

## API 约定

- 统一响应体：`{ "code": 200, "message": "success", "data": ... }`（业务失败时 `code` 非 200）  
- 除 `POST /api/auth/login` 与放行路径外，请求需头：`Authorization: Bearer <token>`  
- 主要前缀示例：  
  - `/api/auth/login`  
  - `/api/user/**`、`/api/drugs/**`、`/api/category/**`  
  - `/api/sale/**`、`/api/purchase/**`、`/api/kcxx/**`  
  - `/api/statistic/**`、`/api/audit/list`  
  - `/api/kb/docs`、`/api/kb/search`、`/api/kb/doc`、`/api/kb/resync`（管理员）  
  - `/api/ai/chat`（药智助手）

## 数据库表（核心业务 9 张）

`userinfo`、`drugs`、`category`、`part`、`purchase`、`sale`、`kcxx`、`repertory`、`counter`；扩展 **`audit_log`** 用于日志审计。

## 开发说明

- Java 与前端均建议使用 **UTF-8**  
- **不要在仓库中写入密钥/密码/API Key**；统一使用环境变量（如 `DB_PASSWORD`、`APP_JWT_SECRET`、`ALI_AI_API_KEY`）  
- 论文/设计说明见仓库上级目录 `智能药店管理系统的设计.md`（技术栈已与当前实现对齐）  

## 安全说明（审查摘要）

| 项 | 说明 |
|----|------|
| 认证 | 无状态 JWT（HS256）；匿名仅 `POST /api/auth/login`（及 OPTIONS 预检、健康检查等），其余需 `Authorization: Bearer` |
| 授权 | URL 级 RBAC：`ROLE_ADMIN` / `ROLE_EMP`；403 返回统一 JSON |
| 注入 | MyBatis 使用 `#{}` 参数化；分页排序在 Service 层规范为布尔/白名单字段，避免动态 SQL 拼接用户输入 |
| CORS | 默认 `http://localhost:*`，生产请改为实际域名并通过环境变量配置 |
| 密钥 | `APP_JWT_SECRET` 生产环境必须替换；`application.yml` 中示例密钥勿用于线上 |
| 前端 | Token 存 `localStorage`，需防范 XSS；生产建议 HTTPS + 合理 CSP |

密码当前为 **MD5** 存储（与历史库表一致）；新系统若升级可考虑迁移为 **BCrypt** 等慢哈希。

## 代码与注释审查摘要

- **后端**：Controller / Service / Mapper 分层清晰；关键安全与排序逻辑集中在 `SecurityConfig`、`*ServiceImpl` 与 Mapper XML。  
- **前端**：列表页统一走服务端分页与 `sortField`/`sortOrder`；路由 `beforeEach` 与 `config/rbac.ts` 避免无权限页面误开。  
- **注释**：Java 类与 `SecurityConfig`、JWT 相关类保留中文说明；不宜在仓库中保留已失效业务角色（如已移除的扩展角色）的残留文档。  

## 许可证

本项目采用 MIT 许可证（若仓库内无 `LICENSE` 文件，请自行补充）。

## 更新日志

### v2.0（开发分支）

- **本地知识库（Markdown）**：新增 `kb/` 目录与 `/api/kb/**`（列表/检索/读取/重建索引）。
- **药智助手**：新增 `/api/ai/chat`，基于知识库检索结果进行“流程问答”；可选启用阿里模型（OpenAI 兼容接口）用于结构化总结与追问。
- **体验优化**：助手浮窗全局入口、引用来源与追问建议（可点击）、中文检索更稳、请求/响应强制 UTF-8。

### v1.1.0

- **RBAC**：管理员全量；员工可操作药品/分类/销售/采购/库存等业务接口，**不可**访问 `/api/user/**`、审计日志；Spring Security + 前端路由/侧栏一致。  
- **登录页**：双栏布局 + 快速选择角色（`expectedPId`）；登录响应携带 `pId`。  
- **列表排序**：分类、库存等多处列表支持服务端按 ID/字段排序（`sortField` / `sortOrder`）；分类/库存 Mapper 使用布尔参数控制排序方向，避免 OGNL 字符串比较歧义。  
- **报表**：销售/采购聚合等（若仓库已包含 `ReportController` / `ReportMapper`）与看板联动。  
- **其它**：JWT 401/403 JSON 与 UTF-8；员工用户创建时间等历史问题已在前序迭代中修复（以代码为准）。

### v1.0.x / 早期

- **v1.0.0**：早期 SSM + Layui 版本（已迁移，README 以当前栈为准）。
