<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Drugs } from '../api/drugs'
import type { Category } from '../api/category'
import * as drugsApi from '../api/drugs'
import * as categoryApi from '../api/category'

const loading = ref(false)
const rows = ref<Drugs[]>([])
const categories = ref<Category[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const sortField = ref<'productTime' | 'id'>('id')
const sortOrder = ref<'asc' | 'desc'>('asc')
const dialog = ref(false)
const isEdit = ref(false)
const form = reactive<Drugs>({
  drugsName: '',
  nums: 10000,
  drugsImage: '/static/img/default.jpg',
  categoryId: 1,
  price: 0,
  people: '成人',
  useMethod: '口服',
  cid: 1,
  rid: 1,
  productTime: '',
  saveTime: 24,
  status: 1,
})

function resetForm() {
  form.id = undefined
  form.drugsName = ''
  form.nums = 10001
  form.drugsImage = '/static/img/default.jpg'
  form.categoryId = categories.value[0]?.categoryId ?? 1
  form.price = 0
  form.people = '成人'
  form.useMethod = '口服'
  form.cid = 1
  form.rid = 1
  form.productTime = new Date().toISOString().slice(0, 10)
  form.saveTime = 24
  form.status = 1
}

async function load() {
  loading.value = true
  try {
    const [d, c] = await Promise.all([
      drugsApi.listDrugs({
        page: page.value,
        size: size.value,
        sortField: sortField.value,
        sortOrder: sortOrder.value,
      }),
      categoryApi.listCategories({ page: 1, size: 200 }),
    ])
    rows.value = d.items
    total.value = d.total
    categories.value = c.items
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
    sortField.value = 'id'
    sortOrder.value = 'asc'
  } else {
    sortField.value = (e.prop === 'productTime' ? 'productTime' : 'id') as 'productTime' | 'id'
    sortOrder.value = e.order === 'descending' ? 'desc' : 'asc'
  }
  page.value = 1
  load()
}

function openAdd() {
  isEdit.value = false
  resetForm()
  dialog.value = true
}

function openEdit(row: Drugs) {
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
    if (isEdit.value) await drugsApi.updateDrug({ ...form })
    else await drugsApi.addDrug({ ...form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function remove(row: Drugs) {
  try {
    await ElMessageBox.confirm(`确定删除「${row.drugsName}」？`, '确认', { type: 'warning' })
    await drugsApi.deleteDrug(row.id!)
    ElMessage.success('已删除')
    await load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function catName(id: number) {
  const c = categories.value.find((x) => x.categoryId === id)
  return c?.categoryName ?? String(id)
}

onMounted(load)
</script>

<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="hdr">
        <span class="ttl">药品管理</span>
        <el-button type="primary" @click="openAdd">新增药品</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="rows" stripe border style="width: 100%" @sort-change="handleSortChange">
      <el-table-column prop="id" label="ID" width="70" sortable="custom" />
      <el-table-column prop="drugsName" label="药品名称" min-width="120" />
      <el-table-column prop="nums" label="编号" width="90" />
      <el-table-column label="分类" width="110">
        <template #default="{ row }">{{ catName(row.categoryId) }}</template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="90" />
      <el-table-column prop="people" label="适用人群" width="100" />
      <el-table-column prop="useMethod" label="用法" width="120" show-overflow-tooltip />
      <el-table-column prop="cid" label="柜台" width="70" />
      <el-table-column prop="rid" label="仓库" width="70" />
      <el-table-column prop="productTime" label="生产日期" width="110" sortable="custom" />
      <el-table-column prop="saveTime" label="保质期(月)" width="100" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">{{ row.status === 1 ? '上架' : '下架' }}</template>
      </el-table-column>
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

    <el-dialog v-model="dialog" :title="isEdit ? '编辑药品' : '新增药品'" width="560px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="药品名称" required>
          <el-input v-model="form.drugsName" />
        </el-form-item>
        <el-form-item label="药品编号">
          <el-input-number v-model="form.nums" :min="1" />
        </el-form-item>
        <el-form-item label="图片路径">
          <el-input v-model="form.drugsImage" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width: 100%">
            <el-option :value="c.categoryId!" :label="c.categoryName" v-for="c in categories" :key="c.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="适用人群">
          <el-input v-model="form.people" />
        </el-form-item>
        <el-form-item label="使用方法">
          <el-input v-model="form.useMethod" />
        </el-form-item>
        <el-form-item label="柜台ID">
          <el-input-number v-model="form.cid" :min="1" />
        </el-form-item>
        <el-form-item label="仓库ID">
          <el-input-number v-model="form.rid" :min="1" />
        </el-form-item>
        <el-form-item label="生产日期">
          <el-date-picker v-model="form.productTime" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="保质期(月)">
          <el-input-number v-model="form.saveTime" :min="1" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option :value="1" label="上架" />
            <el-option :value="0" label="下架" />
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

.pager {
  margin-top: 14px;
  display: flex;
  justify-content: flex-end;
}
</style>
