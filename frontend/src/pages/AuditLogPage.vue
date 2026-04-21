<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { AuditLog } from '../api/audit'
import * as auditApi from '../api/audit'

const loading = ref(false)
const rows = ref<AuditLog[]>([])
const keyword = ref('')

async function load() {
  loading.value = true
  try {
    rows.value = await auditApi.listAuditLogs()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败，请确认已执行 audit_log.sql 创建表')
  } finally {
    loading.value = false
  }
}

function filtered() {
  const k = keyword.value.trim()
  if (!k) return rows.value
  return rows.value.filter(
    (r) =>
      r.operator.includes(k) ||
      r.module.includes(k) ||
      r.action.includes(k) ||
      (r.detail && r.detail.includes(k)) ||
      (r.ip && r.ip.includes(k)),
  )
}

onMounted(load)
</script>

<template>
  <div class="audit-page">
    <el-card v-loading="loading" shadow="never" class="panel">
      <template #header>
        <div class="head">
          <span class="title">日志审计</span>
          <span class="hint">数据来源：数据库表 audit_log（需执行 src/main/resources/audit_log.sql）</span>
        </div>
      </template>

      <div class="toolbar">
        <el-input
          v-model="keyword"
          clearable
          placeholder="按操作人、模块、操作、描述、IP 筛选"
          style="max-width: 360px"
        />
        <el-button @click="load">刷新</el-button>
      </div>

      <el-table :data="filtered()" stripe border style="width: 100%" class="table">
        <el-table-column prop="createdAt" label="时间" width="180" />
        <el-table-column prop="operator" label="操作人" width="110" />
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="action" label="操作类型" width="120" />
        <el-table-column prop="detail" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="来源 IP" width="130" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.audit-page {
  max-width: 1200px;
}

.panel {
  border-radius: 10px;
}

.head {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.hint {
  font-size: 13px;
  color: #909399;
  font-weight: 400;
}

.toolbar {
  margin-bottom: 16px;
  display: flex;
  gap: 12px;
  align-items: center;
}

.table {
  margin-top: 4px;
}
</style>
