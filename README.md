# 智能药店管理系统（Pysystem）

> 帮传统药店数字化管理进销存、员工、库存，还能用 AI 回答业务问题。

[![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)](../LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?style=flat)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue-3-blue?style=flat)](https://vuejs.org)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat&logo=docker)](docker-compose.yml)

---

## 目录

- [截图](#截图)
- [它是什么](#它是什么)
- [为什么做](#为什么做)
- [核心功能](#核心功能)
- [快速开始](#快速开始)
- [使用示例](#使用示例)
- [技术架构](#技术架构)
- [自动化测试](#自动化测试)
- [Roadmap](#roadmap)
- [FAQ](#faq)
- [谁适合看这个](#谁适合看这个)
- [关于我](#关于我)

---

## 截图

<img width="2560" height="1346" alt="主界面" src="https://github.com/user-attachments/assets/6f69b593-3bf5-4c12-9ee2-56971acab981" />
<img width="2545" height="1346" alt="药品管理" src="https://github.com/user-attachments/assets/f6140143-70ed-4b24-a348-629ea848526e" />
<img width="960" height="456" alt="看板" src="https://github.com/user-attachments/assets/ac6e6e43-0469-4bba-a808-cb6bab093746" />
<img width="960" height="456" alt="统计" src="https://github.com/user-attachments/assets/fc552ffb-7940-4bb6-a1e6-58d2fb7624f4" /> -->

---

## 它是什么

Pysystem 是一个**药店后台管理系统**，帮药店管理员工、药品、采购、销售、库存，还能用 AI 回答业务操作问题。

**你可以用它来：**
- 管理员工账号和权限（管理员 / 员工两种角色）
- 维护药品信息、分类、库存，低库存自动预警
- 记录采购和销售流水，生成统计图表
- 用 AI 助手回答"怎么操作系统"这类问题（基于本地知识库 + 可选大模型）

Docker 一键启动，不用手动装 MySQL 和 Java 环境。

---

## 为什么做

很多中小药店还在用纸质台账或 Excel 管理进销存，效率低、容易出错、查账麻烦。

这个项目的目标是：**给药店一个开箱即用的数字化管理工具**——有看板、有统计、有权限控制、有审计日志，还能用 AI 回答操作问题。

从毕业设计的 SSM + Layui 版本，升级到了 Spring Boot 3 + Vue 3 前后端分离架构，同时加了知识库和 AI 助手模块。

---

## 核心功能

| 你能做什么 | 说明 |
|-----------|------|
| **看仪表盘** | 一眼看到 SKU 数量、库存预警、本月销售额、采购趋势 |
| **管员工** | 创建/编辑/删除员工账号，分配管理员或员工角色 |
| **管药品** | 药品信息维护、分类管理 |
| **记采购** | 采购记录录入、查询、统计 |
| **记销售** | 销售记录录入、查询、统计 |
| **看库存** | 库存数量维护，低于 60 自动预警 |
| **看图表** | 销售结构饼图、采购分布柱状图 |
| **查日志** | 操作审计日志，谁在什么时候做了什么 |
| **AI 问答** | 药智助手：问它"怎么导出报表"，它从知识库找答案告诉你 |

---

## 快速开始

### Docker 一键启动（推荐）

```bash
# 1. 进入项目目录
cd pysystem

# 2. 一键启动（MySQL + 后端 + 前端）
docker compose up -d --build
```

启动后打开浏览器：
- 前端：`http://127.0.0.1:5173`
- 后端健康检查：`http://127.0.0.1:8080/actuator/health`

默认测试账号：

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | `admin` | `admin123` |
| 员工 | `emp02` | `employee123` |

### 手动启动（开发者）

```bash
# 1. 导入数据库
mysql -u root -p --default-character-set=utf8mb4 < src/main/resources/pharmacy_system.sql
mysql -u root -p --default-character-set=utf8mb4 < src/main/resources/audit_log.sql

# 2. 启动后端（需要 JDK 17 + Maven）
cd pysystem
$env:DB_PASSWORD="你的数据库密码"
$env:APP_JWT_SECRET="change-me-change-me-change-me-change-me"
mvn -DskipTests spring-boot:run

# 3. 启动前端（需要 Node.js 18+）
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`

---

## 使用示例

### 场景 1：查看经营数据

登录后直接看仪表盘——SKU 总数、库存预警数量、本月销售额，一目了然。

### 场景 2：录入一笔销售

1. 点击「销售管理」
2. 新增销售记录，选择药品、数量、金额
3. 保存后自动更新库存和统计图表

### 场景 3：问 AI 助手

1. 点击右下角「药智助手」浮窗
2. 输入："怎么添加新员工？"
3. AI 从本地知识库检索答案，告诉你操作步骤

---

## 技术架构

```
┌──────────────────────────────────────────┐
│              Frontend (Vue 3)             │
│  Vite · Element Plus · ECharts · Pinia  │
├──────────────────────────────────────────┤
│         Backend (Spring Boot 3)          │
│  Spring Security + JWT · MyBatis        │
│  14 Controllers · AI Chat · KB API      │
├──────────────────────────────────────────┤
│           MySQL 8.x                      │
│  9 张业务表 + audit_log                  │
├──────────────────────────────────────────┤
│  Docker Compose: MySQL + Backend + Frontend│
│  GitHub Actions: Build + Test + Report   │
└──────────────────────────────────────────┘
```

---

## 自动化测试

| 类型 | 技术 | 覆盖内容 |
|------|------|----------|
| API 测试 | pytest + requests | 健康检查、登录鉴权、核心业务接口 |
| E2E 测试 | Playwright | 登录页、权限路由、核心页面访问 |
| CI/CD | GitHub Actions | 自动构建 + Docker 启动 + 全量测试 |

---

## Roadmap

- [x] 前后端分离架构（Spring Boot 3 + Vue 3）
- [x] JWT 认证 + RBAC 权限控制
- [x] 进销存全模块（员工/药品/采购/销售/库存）
- [x] 统计图表（饼图 + 柱状图）
- [x] Docker 一键启动
- [x] 本地知识库 + AI 药智助手
- [x] 操作审计日志
- [x] GitHub Actions CI/CD
- [ ] 移动端适配
- [ ] 多门店支持
- [ ] 导出 Excel 报表
- [ ] 接入更多大模型

---

## FAQ

**Q: 需要什么环境才能运行？**
A: 最简单的方式是 Docker，一条命令全搞定。手动启动需要 JDK 17 + Maven + MySQL 8 + Node.js 18。

**Q: AI 助手需要联网吗？**
A: 知识库问答不需要联网（纯本地 Markdown 检索）。如果想用大模型生成更智能的回答，需要配置阿里 DashScope API Key。

**Q: 密码安全吗？**
A: 当前版本用 MD5 存储密码，适合学习和演示。生产环境建议迁移到 BCrypt。

**Q: 支持多人同时使用吗？**
A: 支持。通过 RBAC 区分管理员和员工角色，员工只能操作业务模块，不能访问用户管理和审计日志。

**Q: 数据会丢失吗？**
A: Docker 模式下数据存在容器内，容器删除后数据会丢失。生产环境建议挂载 MySQL 数据卷。

---

## 谁适合看这个

- **计算机专业学生**：毕业设计参考，完整的前后端分离 + CI/CD + AI 集成案例
- **学 Java 全栈的开发者**：Spring Boot 3 + Vue 3 + MyBatis + JWT + RBAC 的实战项目
- **想了解 AI 集成的人**：知识库检索 + 大模型调用的轻量级 RAG 实现
- **药店 / 零售行业从业者**：可以直接拿来用或二次开发

---

## 关于我

我是**肖恩沃尔特**（Sean Walter），一个从测试工程师正在转型为 AI 独立开发者的程序员。

这个项目从毕业设计起步，逐步升级为一个完整的全栈 + AI 项目。它也是我学习 Spring Boot、Vue 3、Docker 和 RAG 的实战练兵场。

- GitHub: [Dream22180971](https://github.com/Dream22180971)
- Twitter/X: [@sean_walter0717](https://x.com/sean_walter0717)
- 博客: [seanwalter.top](https://seanwalter.top)

---

## License

[MIT](../LICENSE)
