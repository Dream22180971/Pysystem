<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Category } from '../api/category'
import * as categoryApi from '../api/category'

const loading = ref(false)
const rows = ref<Category[]>([])
const dialog = ref(false)
const isEdit = ref(false)
const form = reactive<Category>({ categoryName: '', status: 1 })

function resetForm() {
  form.categoryId = undefined
  form.categoryName = ''
  form.status = 1
}

async function load() {
  loading.value = true
  try {
    rows.value = await categoryApi.listCategories()
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

function openEdit(row: Category) {
  isEdit.value = true
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  if (!form.categoryName.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  try {
    if (isEdit.value) await categoryApi.updateCategory({ ...form })
    else await categoryApi.addCategory({ categoryName: form.categoryName, status: form.status })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function remove(row: Category) {
  try {
    await ElMessageBox.confirm(`确定删除分类「${row.categoryName}」？`, '确认', { type: 'warning' })
    await categoryApi.deleteCategory(row.categoryId!)
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
        <span class="ttl">分类管理</span>
        <el-button type="primary" @click="openAdd">新增分类</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="rows" stripe border style="width: 100%">
      <el-table-column prop="categoryId" label="ID" width="90" />
      <el-table-column prop="categoryName" label="分类名称" min-width="140" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">{{ row.status === 1 ? '可用' : '不可用' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="isEdit ? '编辑分类' : '新增分类'" width="420px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="名称" required>
          <el-input v-model="form.categoryName" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option :value="1" label="可用" />
            <el-option :value="0" label="不可用" />
          </el-select>
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
