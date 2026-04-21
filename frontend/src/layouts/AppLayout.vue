<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import {
  Odometer,
  User,
  Goods,
  PriceTag,
  TrendCharts,
  Box,
  ShoppingCart,
  PieChart,
  Document,
  SwitchButton,
} from '@element-plus/icons-vue'

const router = useRouter()
const auth = useAuthStore()

const welcomeSuffix = computed(() => {
  switch (auth.role) {
    case 'ROLE_ADMIN':
      return '管理员'
    case 'ROLE_EMP':
      return '员工'
    default:
      return auth.username ?? '用户'
  }
})

async function logout() {
  auth.clear()
  await router.replace({ name: 'login' })
}
</script>

<template>
  <el-container class="layout-root">
    <el-aside width="232px" class="layout-aside">
      <div class="aside-inner">
        <div class="logo-block">
          <div class="logo-mark" aria-hidden="true">∞</div>
          <div class="logo-text">
            <div class="logo-title">智慧药房</div>
            <div class="logo-sub">智能药店管理系统</div>
          </div>
        </div>

        <el-menu
          router
          :default-active="$route.path"
          class="side-menu"
          background-color="#001529"
          text-color="rgba(255,255,255,0.72)"
          active-text-color="#ffffff"
        >
          <el-menu-item index="/">
            <el-icon><Odometer /></el-icon>
            <span>智能看板</span>
          </el-menu-item>
          <el-menu-item index="/employees">
            <el-icon><User /></el-icon>
            <span>员工管理</span>
          </el-menu-item>
          <el-menu-item index="/drugs">
            <el-icon><Goods /></el-icon>
            <span>药品管理</span>
          </el-menu-item>
          <el-menu-item index="/categories">
            <el-icon><PriceTag /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
          <el-menu-item index="/sales">
            <el-icon><TrendCharts /></el-icon>
            <span>销售管理</span>
          </el-menu-item>
          <el-menu-item index="/inventory">
            <el-icon><Box /></el-icon>
            <span>库存管理</span>
          </el-menu-item>
          <el-menu-item index="/purchase">
            <el-icon><ShoppingCart /></el-icon>
            <span>采购管理</span>
          </el-menu-item>
          <el-menu-item index="/stats">
            <el-icon><PieChart /></el-icon>
            <span>智能统计</span>
          </el-menu-item>
          <el-menu-item index="/audit-logs">
            <el-icon><Document /></el-icon>
            <span>日志审计</span>
          </el-menu-item>
        </el-menu>

        <div class="aside-footer">
          <el-button class="logout-btn" text @click="logout">
            <el-icon class="logout-ico"><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
      </div>
    </el-aside>

    <el-container class="layout-main-wrap">
      <el-header class="layout-header" height="56px">
        <span class="header-text">
          管理后台 <span class="header-sep">|</span> 欢迎回来，{{ welcomeSuffix }}
        </span>
      </el-header>
      <el-main class="layout-main">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-root {
  min-height: 100vh;
}

.layout-aside {
  background: #001529;
  color: #fff;
}

.aside-inner {
  display: flex;
  flex-direction: column;
  height: 100vh;
  border-right: 1px solid rgba(255, 255, 255, 0.06);
}

.logo-block {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 16px 18px;
  flex-shrink: 0;
}

.logo-mark {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #1890ff, #096dd9);
  display: grid;
  place-items: center;
  font-size: 22px;
  font-weight: 700;
  color: #fff;
}

.logo-title {
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 0.5px;
  color: #fff;
}

.logo-sub {
  margin-top: 2px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.55);
  line-height: 1.3;
}

.side-menu {
  flex: 1;
  border-right: none !important;
  overflow-y: auto;
}

.side-menu :deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  margin: 2px 8px;
  border-radius: 8px;
}

.side-menu :deep(.el-menu-item.is-active) {
  background: #1890ff !important;
  color: #fff !important;
}

.side-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.08) !important;
}

.side-menu :deep(.el-menu-item.is-active:hover) {
  background: #1890ff !important;
}

.aside-footer {
  flex-shrink: 0;
  padding: 12px 16px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.logout-btn {
  width: 100%;
  justify-content: flex-start;
  color: rgba(255, 255, 255, 0.75);
  font-size: 14px;
}

.logout-btn:hover {
  color: #fff;
}

.logout-ico {
  margin-right: 8px;
}

.layout-main-wrap {
  background: #f0f2f5;
  min-height: 100vh;
}

.layout-header {
  display: flex;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.04);
}

.header-text {
  font-size: 15px;
  color: #303133;
  font-weight: 500;
}

.header-sep {
  margin: 0 10px;
  color: #dcdfe6;
  font-weight: 400;
}

.layout-main {
  padding: 20px 24px 28px;
  box-sizing: border-box;
}
</style>
