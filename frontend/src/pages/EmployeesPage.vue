<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Userinfo } from '../api/user'
import * as userApi from '../api/user'

const loading = ref(false)
const rows = ref<Userinfo[]>([])
const dialog = ref(false)
const isEdit = ref(false)
const form = reactive<Userinfo>({
  username: '',
  password: '',
  nickname: '',
  sex: '男',
  age: 18,
  phone: '',
  address: '',
  pId: 2,
  status: 1,
})

function resetForm() {
  form.id = undefined
  form.username = ''
  form.password = ''
  form.nickname = ''
  form.sex = '男'
  form.age = 18
  form.phone = ''
  form.address = ''
  form.pId = 2
  form.status = 1
}

async function load() {
  loading.value = true
  try {
    rows.value = await userApi.listUsers()
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

function openEdit(row: Userinfo) {
  isEdit.value = true
  Object.assign(form, {
    id: row.id,
    username: row.username,
    password: '',
    nickname: row.nickname,
    sex: row.sex,
    age: row.age,
    phone: row.phone,
    address: row.address,
    pId: row.pId,
    status: row.status,
  })
  dialog.value = true
}

async function save() {
  if (!form.username.trim()) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!isEdit.value && !form.password?.trim()) {
    ElMessage.warning('请输入初始密码')
    return
  }
  try {
    if (isEdit.value) {
      const payload = { ...form }
      if (!payload.password?.trim()) delete (payload as Record<string, unknown>).password
      await userApi.updateUser(payload)
      ElMessage.success('已保存')
    } else {
      await userApi.addUser({ ...form })
      ElMessage.success('已添加')
    }
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function remove(row: Userinfo) {
  try {
    await ElMessageBox.confirm(`确定删除用户「${row.username}」？`, '确认', { type: 'warning' })
    await userApi.deleteUser(row.id!)
    ElMessage.success('已删除')
    await load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function roleName(pId: number) {
  if (pId === 1) return '管理员'
  if (pId === 2) return '员工'
  return String(pId)
}

onMounted(load)
</script>

<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="hdr">
        <span class="ttl">员工管理</span>
        <el-button type="primary" @click="openAdd">新增用户</el-button>
      </div>
    </template>

    <el-table v-loading="loading" :data="rows" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="登录名" min-width="110" />
      <el-table-column prop="nickname" label="昵称" min-width="100" />
      <el-table-column prop="sex" label="性别" width="70" />
      <el-table-column prop="age" label="年龄" width="70" />
      <el-table-column prop="phone" label="手机" min-width="120" />
      <el-table-column prop="address" label="地址" min-width="120" show-overflow-tooltip />
      <el-table-column label="角色" width="90">
        <template #default="{ row }">{{ roleName(row.pId) }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">{{ row.status === 1 ? '启用' : '禁用' }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="isEdit ? '编辑用户' : '新增用户'" width="520px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" :disabled="isEdit" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="isEdit ? '新密码' : '密码'" :required="!isEdit">
          <el-input v-model="form.password" type="password" show-password :placeholder="isEdit ? '留空则不修改' : ''" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="1" :max="120" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.pId" style="width: 100%">
            <el-option :value="1" label="管理员" />
            <el-option :value="2" label="员工" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option :value="1" label="启用" />
            <el-option :value="0" label="禁用" />
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
