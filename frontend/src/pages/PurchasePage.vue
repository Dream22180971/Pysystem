<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Purchase } from '../api/purchase'
import * as purchaseApi from '../api/purchase'

const loading = ref(false)
const rows = ref<Purchase[]>([])
const dialog = ref(false)
const isEdit = ref(false)
const form = reactive<Purchase>({
  drugsName: '',
  num: 1,
  indate: '',
  rid: 1,
  marks: '',
})

function resetForm() {
  form.pid = undefined
  form.drugsName = ''
  form.num = 1
  form.indate = new Date().toISOString().slice(0, 10)
  form.rid = 1
  form.marks = ''
}

async function load() {
  loading.value = true
  try {
    rows.value = await purchaseApi.listPurchases()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openAdd() {
  isEdit.value = false
  resetForm()
  dialog.value = true
}

function openEdit(row: Purchase) {
  isEdit.value = true
  Object.assign(form, {
    pid: row.pid,
    drugsName: row.drugsName,
    num: row.num,
    indate: typeof row.indate === 'string' ? row.indate.slice(0, 10) : String(row.indate).slice(0, 10),
    rid: row.rid,
    marks: row.marks ?? '',
  })
  dialog.value = true
}

async function save() {
  if (!form.drugsName.trim()) {
    ElMessage.warning('请输入药品名称')
    return
  }
  try {
    if (isEdit.value) await purchaseApi.updatePurchase({ ...form })
    else await purchaseApi.addPurchase({ ...form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function remove(row: Purchase) {
  try {
    await ElMessageBox.confirm('确定删除该采购记录？', '确认', { type: 'warning' })
    await purchaseApi.deletePurchase(row.pid!)
    ElMessage.success('已删除')
    await load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function fmtDate(v: string | unknown) {
  if (v == null) return ''
  const s = String(v)
  return s.length >= 10 ? s.slice(0, 10) : s
}

onMounted(load)
</script>

<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="hdr">
        <span class="ttl">采购管理</span>
        <el-button type="primary" @click="openAdd">新增采购</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="rows" stripe border style="width: 100%">
      <el-table-column prop="pid" label="ID" width="70" />
      <el-table-column prop="drugsName" label="药品名称" min-width="120" />
      <el-table-column prop="num" label="采购数量" width="100" />
      <el-table-column label="进货日期" width="120">
        <template #default="{ row }">{{ fmtDate(row.indate) }}</template>
      </el-table-column>
      <el-table-column prop="rid" label="仓库ID" width="90" />
      <el-table-column prop="marks" label="备注" min-width="140" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="isEdit ? '编辑采购' : '新增采购'" width="500px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="药品名称" required>
          <el-input v-model="form.drugsName" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.num" :min="1" />
        </el-form-item>
        <el-form-item label="进货日期">
          <el-date-picker v-model="form.indate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="仓库ID">
          <el-input-number v-model="form.rid" :min="1" />
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
</style>
