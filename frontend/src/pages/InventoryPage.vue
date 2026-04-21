<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Kcxx } from '../api/kcxx'
import * as kcxxApi from '../api/kcxx'

const loading = ref(false)
const rows = ref<Kcxx[]>([])
const dialog = ref(false)
const isEdit = ref(false)
const form = reactive<Kcxx>({
  drugsName: '',
  num: 0,
  rid: 1,
  marks: '',
})

function resetForm() {
  form.kid = undefined
  form.drugsName = ''
  form.num = 0
  form.rid = 1
  form.marks = ''
}

async function load() {
  loading.value = true
  try {
    rows.value = await kcxxApi.listKcxx()
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

function openEdit(row: Kcxx) {
  isEdit.value = true
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  if (!form.drugsName.trim()) {
    ElMessage.warning('请输入药品名称')
    return
  }
  try {
    if (isEdit.value) await kcxxApi.updateKcxx({ ...form })
    else await kcxxApi.addKcxx({ ...form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function remove(row: Kcxx) {
  try {
    await ElMessageBox.confirm(`确定删除库存记录「${row.drugsName}」？`, '确认', { type: 'warning' })
    await kcxxApi.deleteKcxx(row.kid!)
    ElMessage.success('已删除')
    await load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="hdr">
        <span class="ttl">库存管理</span>
        <el-button type="primary" @click="openAdd">新增库存</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="rows" stripe border style="width: 100%">
      <el-table-column prop="kid" label="ID" width="70" />
      <el-table-column prop="drugsName" label="药品名称" min-width="140" />
      <el-table-column prop="num" label="库存数量" width="100" />
      <el-table-column prop="rid" label="仓库ID" width="90" />
      <el-table-column prop="marks" label="备注" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="isEdit ? '编辑库存' : '新增库存'" width="480px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="药品名称" required>
          <el-input v-model="form.drugsName" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.num" :min="0" />
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
