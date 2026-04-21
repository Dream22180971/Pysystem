<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
} from 'echarts/components'
import VChart from 'vue-echarts'
import * as reportApi from '../api/report'

use([
  CanvasRenderer,
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
])

const loading = ref(true)
const start = ref<string>('')
const end = ref<string>('')
const threshold = ref(10)

const queryParams = computed(() => {
  const s = start.value?.trim() || undefined
  const e = end.value?.trim() || undefined
  return { start: s, end: e }
})

const COLORS = ['#5b8ff9', '#5ad8a6', '#5d7092', '#f6bd16', '#e8684a', '#945fb9', '#ff9845']

const saleDrugOption = ref<Record<string, unknown>>({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left', top: 'middle' },
  series: [
    {
      type: 'pie',
      radius: ['35%', '65%'],
      center: ['58%', '50%'],
      label: { formatter: '{b}\n{d}%' },
      data: [] as { value: number; name: string; itemStyle?: { color: string } }[],
    },
  ],
})

const saleTrendOption = ref<Record<string, unknown>>({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '12%', containLabel: true },
  xAxis: { type: 'category', data: [] as string[], axisLabel: { rotate: 24 } },
  yAxis: { type: 'value', name: '销售额', splitLine: { lineStyle: { type: 'dashed' } } },
  series: [{ type: 'bar', data: [] as number[], itemStyle: { color: '#1890ff' }, barWidth: '50%' }],
})

const purchaseBarOption = ref<Record<string, unknown>>({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '12%', containLabel: true },
  xAxis: { type: 'category', data: [] as string[], axisLabel: { rotate: 24 } },
  yAxis: { type: 'value', name: '采购数量', splitLine: { lineStyle: { type: 'dashed' } } },
  series: [{ type: 'bar', data: [] as number[], itemStyle: { color: '#52c41a' }, barWidth: '50%' }],
})

const lowStockRows = ref<{ drugsName: string; qty: number }[]>([])

async function load() {
  loading.value = true
  try {
    const [saleDrug, saleDay, purchaseDrug, lowStock] = await Promise.all([
      reportApi.salesByDrug({ ...queryParams.value, limit: 20 }),
      reportApi.salesByDay(queryParams.value),
      reportApi.purchaseByDrug({ ...queryParams.value, limit: 20 }),
      reportApi.lowStock({ threshold: threshold.value, limit: 50 }),
    ])

    const pieData = saleDrug.map((p, i) => ({
      value: p.qty,
      name: p.drugsName || '未知药品',
      itemStyle: { color: COLORS[i % COLORS.length] },
    }))
    const s = saleDrugOption.value.series as Record<string, unknown>[]
    if (s[0]) s[0].data = pieData

    const dayNames = saleDay.map((d) => d.day)
    const dayAmount = saleDay.map((d) => Number(d.amount || 0))
    const x1 = saleTrendOption.value.xAxis as Record<string, unknown>
    x1.data = dayNames
    const ser1 = saleTrendOption.value.series as Record<string, unknown>[]
    if (ser1[0]) ser1[0].data = dayAmount

    const pNames = purchaseDrug.map((d) => d.drugsName)
    const pQty = purchaseDrug.map((d) => d.qty)
    const x2 = purchaseBarOption.value.xAxis as Record<string, unknown>
    x2.data = pNames
    const ser2 = purchaseBarOption.value.series as Record<string, unknown>[]
    if (ser2[0]) ser2[0].data = pQty

    lowStockRows.value = lowStock.map((r) => ({ drugsName: r.drugsName, qty: r.qty }))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <div v-loading="loading" class="stats">
    <el-alert
      title="数据报表说明"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
      description="本页报表直接基于系统业务表 sale / purchase / kcxx 聚合生成，可按时间范围筛选。"
    />

    <el-card shadow="never" class="panel-card" style="margin-bottom: 16px">
      <template #header>
        <span class="panel-title">筛选条件</span>
      </template>
      <el-form label-width="90px" inline>
        <el-form-item label="开始日期">
          <el-date-picker v-model="start" type="date" value-format="YYYY-MM-DD" placeholder="不限" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="end" type="date" value-format="YYYY-MM-DD" placeholder="不限" />
        </el-form-item>
        <el-form-item label="库存阈值">
          <el-input-number v-model="threshold" :min="0" :max="999999" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load">生成报表</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">销售结构（按药品汇总数量）</span>
          </template>
          <v-chart class="chart" :option="saleDrugOption" autoresize />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">销售趋势（按天汇总销售额）</span>
          </template>
          <v-chart class="chart" :option="saleTrendOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">采购排行（按药品汇总数量）</span>
          </template>
          <v-chart class="chart" :option="purchaseBarOption" autoresize />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">库存预警（库存 ≤ 阈值）</span>
          </template>
          <el-table :data="lowStockRows" stripe style="width: 100%">
            <el-table-column prop="drugsName" label="药品名称" min-width="140" />
            <el-table-column prop="qty" label="当前库存" width="120" />
            <template #empty>
              <div class="table-empty">暂无预警数据</div>
            </template>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stats {
  max-width: 1200px;
}
.panel-card {
  border-radius: 10px;
  margin-bottom: 16px;
}
.panel-title {
  font-size: 15px;
  font-weight: 600;
}
.chart {
  height: 380px;
  width: 100%;
}
.table-empty {
  padding: 28px 0;
  text-align: center;
  color: #909399;
  font-size: 14px;
}
</style>
