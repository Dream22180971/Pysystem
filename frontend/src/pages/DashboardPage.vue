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
import { WarningFilled, Coin, GoodsFilled } from '@element-plus/icons-vue'
import * as drugsApi from '../api/drugs'
import type { Kcxx } from '../api/kcxx'
import * as kcxxApi from '../api/kcxx'
import * as saleApi from '../api/sale'
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
const statSku = ref(0)
const statWarn = ref(0)
const statSales = ref('¥ 0')
const warnRows = ref<Kcxx[]>([])

const COLORS = ['#5b8ff9', '#5ad8a6', '#5d7092', '#f6bd16', '#e8684a', '#945fb9', '#ff9845']

const pieOption = ref<Record<string, unknown>>({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left', top: 'middle', textStyle: { fontSize: 12 } },
  series: [
    {
      name: '销量占比',
      type: 'pie',
      radius: ['38%', '68%'],
      center: ['58%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
      label: { formatter: '{b}\n{d}%', fontSize: 11 },
      data: [] as { value: number; name: string; itemStyle?: { color: string } }[],
    },
  ],
})

const barOption = ref<Record<string, unknown>>({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '18%', containLabel: true },
  xAxis: { type: 'category', data: [] as string[], axisLabel: { fontSize: 11, rotate: 28 } },
  yAxis: { type: 'value', name: '采购数量', splitLine: { lineStyle: { type: 'dashed' } } },
  series: [
    {
      name: '采购量',
      type: 'bar',
      barWidth: '52%',
      data: [] as number[],
      itemStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: '#69c0ff' },
            { offset: 1, color: '#1890ff' },
          ],
        },
        borderRadius: [4, 4, 0, 0],
      },
    },
  ],
})

async function load() {
  loading.value = true
  try {
    const [drugs, warn, sales, pie, bar] = await Promise.all([
      drugsApi.listDrugs(),
      kcxxApi.listWarning(),
      saleApi.listSales(),
      statisticApi.getSalePie(),
      statisticApi.getPurchaseBar(),
    ])
    statSku.value = drugs.length
    statWarn.value = warn.length
    warnRows.value = warn

    const now = new Date()
    let sum = 0
    for (const s of sales) {
      const raw = s.saledate as string | number | Date
      const d = raw instanceof Date ? raw : new Date(typeof raw === 'string' ? raw : Number(raw))
      if (!Number.isNaN(d.getTime()) && d.getFullYear() === now.getFullYear() && d.getMonth() === now.getMonth()) {
        sum += parseFloat(String(s.total)) || 0
      }
    }
    statSales.value = sum >= 1000 ? `¥ ${(sum / 1000).toFixed(1)}k` : `¥ ${sum.toFixed(2)}`

    const pieData = pie.map((p, i) => ({
      value: p.value,
      name: p.name,
      itemStyle: { color: COLORS[i % COLORS.length] },
    }))
    const s0 = pieOption.value.series as Record<string, unknown>[]
    if (s0[0]) s0[0].data = pieData

    const names = bar.map((b) => b.name)
    const vals = bar.map((b) => b.value)
    const x = barOption.value.xAxis as Record<string, unknown>
    x.data = names
    const maxV = Math.max(10, ...vals)
    const y = barOption.value.yAxis as Record<string, unknown>
    y.max = Math.ceil(maxV / 100) * 100 || 500
    const ser = barOption.value.series as Record<string, unknown>[]
    if (ser[0]) ser[0].data = vals
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '看板数据加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <div v-loading="loading" class="dashboard">
    <el-row :gutter="16" class="weather-row">
      <el-col :span="24">
        <el-card shadow="never" class="panel-card weather-card">
          <template #header>
            <span class="panel-title">智能天气 · 用药推荐</span>
          </template>
          <div class="weather-body">
            <div class="weather-loc">徐州市</div>
            <div class="weather-main">
              <span class="weather-deg">18°</span>
              <span class="weather-desc">多云 · 空气质量良</span>
            </div>
            <p class="weather-tip">
              昼夜温差较大，注意保暖；过敏性鼻炎高发季，可备抗过敏类药品（演示文案）。
            </p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="stat-row">
      <el-col :xs="24" :sm="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-inner">
            <div class="stat-icon blue">
              <el-icon :size="26"><GoodsFilled /></el-icon>
            </div>
            <div>
              <div class="stat-label">药品总 SKU</div>
              <div class="stat-value">{{ statSku }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-inner">
            <div class="stat-icon orange">
              <el-icon :size="26"><WarningFilled /></el-icon>
            </div>
            <div>
              <div class="stat-label">库存预警药品</div>
              <div class="stat-value danger">{{ statWarn }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-inner">
            <div class="stat-icon cyan">
              <el-icon :size="26"><Coin /></el-icon>
            </div>
            <div>
              <div class="stat-label">本月销售额</div>
              <div class="stat-value">{{ statSales }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">药品销量比例统计（按销售数量聚合）</span>
          </template>
          <v-chart class="chart" :option="pieOption" autoresize />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">采购热榜（按采购数量聚合）</span>
          </template>
          <v-chart class="chart chart-bar" :option="barOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="24">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <span class="panel-title">库存预警信息（库存 &lt; 60）</span>
          </template>
          <el-table :data="warnRows" stripe style="width: 100%">
            <el-table-column prop="drugsName" label="药品名称" min-width="140" />
            <el-table-column prop="num" label="当前库存" width="120" />
            <el-table-column prop="rid" label="仓库ID" width="100" />
            <el-table-column prop="marks" label="备注" min-width="120" show-overflow-tooltip />
            <el-table-column label="预警状态" width="120">
              <template #default>库存不足</template>
            </el-table-column>
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
.dashboard {
  max-width: 1400px;
}

.weather-row {
  margin-bottom: 8px;
}

.stat-row {
  margin-bottom: 8px;
}

.stat-card {
  border-radius: 10px;
  margin-bottom: 16px;
}

.stat-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  color: #fff;
}

.stat-icon.blue {
  background: linear-gradient(135deg, #69c0ff, #1890ff);
}

.stat-icon.orange {
  background: linear-gradient(135deg, #ffc069, #fa8c16);
}

.stat-icon.cyan {
  background: linear-gradient(135deg, #87e8de, #13c2c2);
}

.stat-label {
  font-size: 13px;
  color: #909399;
}

.stat-value {
  margin-top: 4px;
  font-size: 26px;
  font-weight: 700;
  color: #303133;
  letter-spacing: 0.5px;
}

.stat-value.danger {
  color: #f5222d;
}

.chart-row {
  margin-bottom: 8px;
}

.panel-card {
  border-radius: 10px;
  margin-bottom: 16px;
}

.panel-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.chart {
  height: 320px;
  width: 100%;
}

.chart-bar {
  height: 340px;
}

.table-empty {
  padding: 28px 0;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.weather-card {
  min-height: 200px;
}

.weather-body {
  padding: 8px 4px 4px;
}

.weather-loc {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.weather-main {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 12px;
}

.weather-deg {
  font-size: 36px;
  font-weight: 700;
  color: #1890ff;
}

.weather-desc {
  font-size: 13px;
  color: #909399;
}

.weather-tip {
  margin: 0;
  font-size: 13px;
  line-height: 1.65;
  color: #606266;
}
</style>
