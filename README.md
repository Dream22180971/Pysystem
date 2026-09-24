<div align="center">

# Pysystem

**A full-stack pharmacy management system for inventory, sales, staff, audit and AI-assisted help.**

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Spring Boot](https://img.shields.io/badge/Spring+Boot-3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3-42B883?style=for-the-badge&logo=vue.js&logoColor=white)](https://vuejs.org)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](docker-compose.yml)
[![Tests](https://img.shields.io/badge/TESTS-API%20%7C%20E2E-7C3AED?style=for-the-badge)](#-testing)

</div>

---

## 🎯 What it is

Pysystem is a full-stack pharmacy operations project covering:

- employee accounts and RBAC
- medicine catalog
- purchasing
- sales
- inventory and low-stock alerts
- dashboards and charts
- audit logs
- local knowledge-base / AI assistant integration

It is primarily an engineering reference project rather than a production-ready pharmacy SaaS.

---

## 🎬 Demo

<div align="center">

<img width="92%" alt="Pysystem dashboard" src="https://github.com/user-attachments/assets/6f69b593-3bf5-4c12-9ee2-56971acab981" />

<img width="92%" alt="Pysystem medicine management" src="https://github.com/user-attachments/assets/f6140143-70ed-4b24-a348-629ea848526e" />

</div>

---

## ⚡ Quick Start

```bash
git clone https://github.com/Dream22180971/Pysystem.git
cd Pysystem

docker compose up -d --build
```

Then open:

- Frontend: `http://127.0.0.1:5173`
- Backend health: `http://127.0.0.1:8080/actuator/health`

Docker Compose starts MySQL, backend and frontend together.

---

## 🧩 Architecture

```mermaid
flowchart TB
    UI[Vue 3 Frontend] --> API[Spring Boot 3 API]
    API --> DB[(MySQL 8)]
    API --> AUTH[JWT / RBAC]
    API --> AUDIT[Audit Log]
    API --> AI[Knowledge / AI Assistant]
    TEST[pytest + Playwright] --> API
    TEST --> UI
```

---

## ✨ Main Modules

| Module | Responsibility |
|---|---|
| Dashboard | operating metrics and alerts |
| Employee | accounts and role control |
| Medicine | catalog and categories |
| Purchase | purchasing records |
| Sales | sales records |
| Inventory | stock management and low-stock alerts |
| Audit | operation trace |
| AI assistant | answer system-operation questions from local knowledge |

---

## 🧪 Testing

| Type | Tooling | Coverage |
|---|---|---|
| API | pytest + requests | health, auth and core APIs |
| E2E | Playwright | login, protected routes and main pages |
| CI | GitHub Actions | build, Docker startup and automated checks |

---

## 🔐 Security & Production Notes

Before real production use, review at least:

- password hashing strategy
- secret management
- persistent database volumes
- HTTPS and reverse proxy configuration
- backup / recovery
- production-grade RBAC and audit requirements

This repository is best treated as a full-stack engineering project and secondary-development base.

---

## 🗺 Roadmap

- [x] Spring Boot 3 + Vue 3
- [x] JWT + RBAC
- [x] employee / medicine / purchase / sales / inventory modules
- [x] dashboard charts
- [x] Docker Compose
- [x] knowledge-base / AI assistant
- [x] audit logs
- [x] automated testing
- [ ] mobile optimization
- [ ] multi-store support
- [ ] Excel export
- [ ] more model providers

---

## 📄 License

MIT

<div align="center">

**A business system is more than CRUD when permissions, audit, testing and operations are designed together.**

</div>
