<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { loginApi } from '../api/auth'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const username = ref('admin')
const password = ref('admin123')
const loading = ref(false)
const error = ref<string | null>(null)

const redirectTo = computed(() => {
  const r = route.query.redirect
  return typeof r === 'string' && r.length > 0 ? r : '/'
})

async function submit() {
  error.value = null
  loading.value = true
  try {
    const res = await loginApi({ username: username.value.trim(), password: password.value })
    if (res.code !== 200 || !res.data) {
      error.value = res.message || '登录失败'
      return
    }
    auth.setAuth({ token: res.data.token, username: res.data.username, role: res.data.role })
    await router.replace(redirectTo.value)
  } catch (e) {
    error.value = '网络错误或服务不可用'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page">
    <div class="card">
      <div class="title">智慧药房后台</div>
      <div class="subtitle">使用账号密码登录</div>

      <form class="form" @submit.prevent="submit">
        <label class="field">
          <span>用户名</span>
          <input v-model="username" autocomplete="username" placeholder="请输入用户名" />
        </label>

        <label class="field">
          <span>密码</span>
          <input v-model="password" type="password" autocomplete="current-password" placeholder="请输入密码" />
        </label>

        <div v-if="error" class="error">{{ error }}</div>

        <button class="btn" type="submit" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <div class="hint">默认账号：admin / admin123</div>
    </div>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: radial-gradient(1200px 600px at 20% 10%, rgba(59, 130, 246, 0.25), transparent),
    radial-gradient(900px 500px at 80% 30%, rgba(16, 185, 129, 0.25), transparent),
    #0b1220;
  padding: 24px;
}

.card {
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);
  border-radius: 18px;
  padding: 24px;
  color: #e5e7eb;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.35);
}

.title {
  font-size: 20px;
  font-weight: 700;
}

.subtitle {
  margin-top: 6px;
  font-size: 13px;
  color: rgba(229, 231, 235, 0.75);
}

.form {
  margin-top: 18px;
  display: grid;
  gap: 12px;
}

.field {
  display: grid;
  gap: 6px;
}

.field span {
  font-size: 12px;
  color: rgba(229, 231, 235, 0.85);
}

input {
  height: 42px;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.14);
  background: rgba(17, 24, 39, 0.55);
  color: #e5e7eb;
  padding: 0 12px;
  outline: none;
}

input:focus {
  border-color: rgba(59, 130, 246, 0.65);
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.18);
}

.error {
  font-size: 12px;
  color: #fecaca;
  background: rgba(239, 68, 68, 0.15);
  border: 1px solid rgba(239, 68, 68, 0.25);
  padding: 10px 12px;
  border-radius: 12px;
}

.btn {
  height: 44px;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  font-weight: 700;
  background: linear-gradient(135deg, #3b82f6, #10b981);
  color: #081018;
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.hint {
  margin-top: 14px;
  font-size: 12px;
  color: rgba(229, 231, 235, 0.65);
}
</style>

