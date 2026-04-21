<script setup lang="ts">
import { onMounted, ref } from 'vue'
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
import * as statisticApi from '../api/statistic'

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
const COLORS = ['#5b8ff9', '#5ad8a6', '#5d7092', '#f6bd16', '#e8684a', '#945fb9', '#ff9845']

const pieOption = ref<Record<string, unknown>>({
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

const barOption = ref<Record<string, unknown>>({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '12%', containLabel: true },
  xAxis: { type: 'category', data: [] as string[], axisLabel: { rotate: 24 } },
  yAxis: { type: 'value', name: '数量', splitLine: { lineStyle: { type: 'dashed' } } },
  series: [{ type: 'bar', data: [] as number[], itemStyle: { color: '#1890ff' }, barWidth: '50%' }],
})

async function load() {
  loading.value = true
  try {
    const [pie, bar] = await Promise.all([statisticApi.getSalePie(), statisticApi.getPurchaseBar()])
    const pieData = pie.map((p, i) => ({
      value: p.value,
      name: p.name,
      itemStyle: { color: COLORS[i % COLORS.length] },
    }))
    const s = pieOption.value.series as Record<string, unknown>[]
    if (s[0]) s[0].data = pieData

    const names = bar.map((b) => b.name)
    const vals = bar.map((b) => b.value)
    const x = barOption.value.xAxis as Record<string, unknown>
    x.data = names
    const ser = barOption.value.series as Record<string, unknown>[]
    if (ser[0]) ser[0].data = vals
    const maxV = Math.max(10, ...vals)
    const y = barOption.value.yAxis as Record<string, unknown>
    y.max = Math.ceil(maxV / 50) * 50 || 100
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
      title="数据说明"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 16px"
      description="销售饼图按「药品名称」汇总销售数量；采购柱状图按「药品名称」汇总采购数量，数据来自数据库 sale / purchase 表。"
    />

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">销售结构（数量）</span>
          </template>
          <v-chart class="chart" :option="pieOption" autoresize />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">采购分布（数量）</span>
          </template>
          <v-chart class="chart" :option="barOption" autoresize />
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
</style>
