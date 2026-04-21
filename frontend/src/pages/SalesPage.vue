<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Sale } from '../api/sale'
import * as saleApi from '../api/sale'

const loading = ref(false)
const rows = ref<Sale[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const sortField = ref<'saledate' | 'saleId'>('saledate')
const sortOrder = ref<'asc' | 'desc'>('asc')
const dialog = ref(false)
const isEdit = ref(false)
const form = reactive<Sale>({
  drugsName: '',
  price: 0,
  num: 1,
  total: '0.00',
  saledate: '',
  marks: '',
})

function syncTotal() {
  const t = (form.price * form.num).toFixed(2)
  form.total = t
}

watch(
  () => [form.price, form.num],
  () => syncTotal(),
  { deep: true },
)

function resetForm() {
  form.saleId = undefined
  form.drugsName = ''
  form.price = 0
  form.num = 1
  form.total = '0.00'
  form.saledate = new Date().toISOString().slice(0, 10)
  form.marks = ''
}

async function load() {
  loading.value = true
  try {
    const res = await saleApi.listSales({
      page: page.value,
      size: size.value,
      sortField: sortField.value,
      sortOrder: sortOrder.value,
    })
    rows.value = res.items
    total.value = res.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(p: number) {
  page.value = p
  load()
}

function handleSizeChange(s: number) {
  size.value = s
  page.value = 1
  load()
}

function handleSortChange(e: { prop?: string; order?: 'ascending' | 'descending' | null }) {
  if (!e?.order || !e?.prop) {
    sortField.value = 'saledate'
    sortOrder.value = 'asc'
  } else {
    sortField.value = (e.prop === 'saleId' ? 'saleId' : 'saledate') as 'saledate' | 'saleId'
    sortOrder.value = e.order === 'descending' ? 'desc' : 'asc'
  }
  page.value = 1
  load()
}

function openAdd() {
  isEdit.value = false
  resetForm()
  syncTotal()
  dialog.value = true
}

function openEdit(row: Sale) {
  isEdit.value = true
  Object.assign(form, {
    saleId: row.saleId,
    drugsName: row.drugsName,
    price: row.price,
    num: row.num,
    total: row.total,
    saledate: typeof row.saledate === 'string' ? row.saledate.slice(0, 10) : String(row.saledate).slice(0, 10),
    marks: row.marks ?? '',
  })
  dialog.value = true
}

async function save() {
  if (!form.drugsName.trim()) {
    ElMessage.warning('请输入药品名称')
    return
  }
  syncTotal()
  try {
    if (isEdit.value) await saleApi.updateSale({ ...form })
    else await saleApi.addSale({ ...form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function remove(row: Sale) {
  try {
    await ElMessageBox.confirm('确定删除该销售记录？', '确认', { type: 'warning' })
    await saleApi.deleteSale(row.saleId!)
    ElMessage.success('已删除')
    await load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function fmtDate(v: string | unknown) {
  if (v == null || String(v).trim() === '') return new Date().toISOString().slice(0, 10)
  const s = String(v)
  return s.length >= 10 ? s.slice(0, 10) : s
}

onMounted(load)
</script>

<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="hdr">
        <span class="ttl">销售管理</span>
        <el-button type="primary" @click="openAdd">新增销售</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="rows" stripe border style="width: 100%" @sort-change="handleSortChange">
      <el-table-column prop="saleId" label="ID" width="70" sortable="custom" />
      <el-table-column prop="drugsName" label="药品名称" min-width="120" />
      <el-table-column prop="price" label="单价" width="90" />
      <el-table-column prop="num" label="数量" width="80" />
      <el-table-column prop="total" label="总价" width="100" />
      <el-table-column prop="saledate" label="销售日期" width="120" sortable="custom">
        <template #default="{ row }">{{ fmtDate(row.saledate) }}</template>
      </el-table-column>
      <el-table-column prop="marks" label="备注" min-width="120" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pager">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <el-dialog v-model="dialog" :title="isEdit ? '编辑销售' : '新增销售'" width="500px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="药品名称" required>
          <el-input v-model="form.drugsName" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.num" :min="1" />
        </el-form-item>
        <el-form-item label="总价">
          <el-input v-model="form.total" readonly />
        </el-form-item>
        <el-form-item label="销售日期">
          <el-date-picker v-model="form.saledate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.marks" type="textarea" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.page-card {
  border-radius: 10px;
}
.hdr {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.ttl {
  font-size: 16px;
  font-weight: 600;
}

.pager {
  margin-top: 14px;
  display: flex;
  justify-content: flex-end;
}
</style>
