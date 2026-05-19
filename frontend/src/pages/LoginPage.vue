<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { loginApi } from '../api/auth'
import { useAuthStore } from '../stores/auth'
import { Document, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)

/** 与后端 part.P_id 一致：1 管理员，2 员工 */
const selectedPId = ref<number>(1)

const roleChoices = [
  { pId: 1, title: '管理员', sub: '系统管理、员工与审计', icon: Document },
  { pId: 2, title: '员工', sub: '药品、库存、销售等业务', icon: UserFilled },
] as const

const redirectTo = computed(() => {
  const r = route.query.redirect
  return typeof r === 'string' && r.length > 0 ? r : '/'
})

async function submit() {
  error.value = null
  loading.value = true
  try {
    const res = await loginApi({
      username: username.value.trim(),
      password: password.value,
      expectedPId: selectedPId.value,
    })
    if (res.code !== 200 || !res.data) {
      error.value = res.message || '登录失败'
      return
    }
    auth.setAuth({
      token: res.data.token,
      username: res.data.username,
      role: res.data.role,
      pId: res.data.pId ?? null,
    })
    await router.replace(redirectTo.value)
  } catch (e: unknown) {
    const ax = e as { response?: { data?: { message?: string } } }
    error.value = ax.response?.data?.message ?? '网络错误或服务不可用'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page">
    <div class="shell">
      <section class="hero" aria-hidden="true">
        <div class="hero-mark">∞</div>
        <h1 class="hero-title">智慧药房</h1>
        <p class="hero-sub">智能药店管理系统 · 药品 · 库存 · 销售一体化</p>
        <ul class="hero-list">
          <li>药品与分类管理</li>
          <li>库存与销售追溯</li>
          <li>数据报表与审计</li>
          <li>多角色权限（RBAC）</li>
        </ul>
      </section>

      <section class="panel">
        <h2 class="panel-title">账号登录</h2>
        <p class="panel-hint">请选择登录身份（与账号在系统中的角色需一致）</p>

        <div class="role-row" role="group" aria-label="快速选择角色">
          <button
            v-for="r in roleChoices"
            :key="r.pId"
            type="button"
            class="role-card"
            :class="{ active: selectedPId === r.pId }"
            :data-testid="`role-${r.pId}`"
            @click="selectedPId = r.pId"
          >
            <el-icon class="role-ico"><component :is="r.icon" /></el-icon>
            <span class="role-title">{{ r.title }}</span>
            <span class="role-sub">{{ r.sub }}</span>
          </button>
        </div>

        <form class="form" data-testid="login-form" @submit.prevent="submit">
          <label class="field">
            <span>用户名</span>
            <el-input v-model="username" autocomplete="username" placeholder="请输入用户名" />

          </label>



          <label class="field">

            <span>密码</span>

            <el-input

              v-model="password"

              type="password"

              autocomplete="current-password"

              placeholder="请输入密码"

              show-password

            />

          </label>



          <div v-if="error" class="error">{{ error }}</div>



          <el-button class="btn-full" type="primary" native-type="submit" :loading="loading" size="large">

            登录

          </el-button>

        </form>



        <p class="foot-hint">
          提示：开发环境可使用种子账号登录（账号信息见后端 `README.md` / `pharmacy_system.sql`）。
        </p>

      </section>

    </div>

  </div>

</template>



<style scoped>

.page {

  min-height: 100vh;

  display: grid;

  place-items: center;

  background: radial-gradient(1200px 600px at 20% 10%, rgba(59, 130, 246, 0.22), transparent),

    radial-gradient(900px 500px at 80% 30%, rgba(16, 185, 129, 0.25), transparent), #0b1220;

  padding: 24px;

}



.shell {

  width: min(100%, 960px);

  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 0;

  border-radius: 20px;

  overflow: hidden;

  border: 1px solid rgba(255, 255, 255, 0.1);

  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.45);

}



@media (max-width: 880px) {

  .shell {

    grid-template-columns: 1fr;

  }

  .hero {

    display: none;

  }

}



.hero {

  background: linear-gradient(160deg, rgba(24, 144, 255, 0.35), rgba(9, 109, 217, 0.12));

  padding: 36px 32px;

  color: #e5e7eb;

}



.hero-mark {

  width: 48px;

  height: 48px;

  border-radius: 12px;

  background: linear-gradient(135deg, #1890ff, #096dd9);

  display: grid;

  place-items: center;

  font-size: 1.4rem;

  font-weight: 800;

  margin-bottom: 16px;

}



.hero-title {

  margin: 0;

  font-size: 1.4rem;

  font-weight: 800;

  letter-spacing: 0.5px;

}



.hero-sub {

  margin: 8px 0 0;

  font-size: 0.85rem;

  color: rgba(229, 231, 235, 0.85);

  line-height: 1.5;

}



.hero-list {

  margin: 22px 0 0;

  padding-left: 1.1rem;

  font-size: 0.88rem;

  line-height: 1.85;

  color: rgba(229, 231, 235, 0.85);

}



.panel {

  background: rgba(17, 24, 39, 0.92);

  padding: 28px 26px 22px;

  color: #e5e7eb;

}



.panel-title {

  margin: 0;

  font-size: 1.15rem;

  font-weight: 700;

}



.panel-hint {

  margin: 8px 0 0;

  font-size: 12px;

  color: rgba(229, 231, 235, 0.65);

}



.role-row {

  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 10px;

  margin-top: 16px;

}



.role-card {

  display: flex;

  flex-direction: column;

  align-items: flex-start;

  gap: 4px;

  padding: 10px 12px;

  border-radius: 12px;

  border: 1px solid rgba(255, 255, 255, 0.12);

  background: rgba(15, 23, 42, 0.6);

  color: inherit;

  cursor: pointer;

  text-align: left;

  transition: border-color 0.15s, background 0.15s;

}



.role-card:hover {

  border-color: rgba(59, 130, 246, 0.45);

}



.role-card.active {

  border-color: rgba(59, 130, 246, 0.85);

  background: rgba(59, 130, 246, 0.12);

}



.role-ico {

  font-size: 18px;

  color: #93c5fd;

}



.role-title {

  font-size: 13px;

  font-weight: 700;

}



.role-sub {

  font-size: 11px;

  color: rgba(229, 231, 235, 0.6);

  line-height: 1.35;

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



.error {

  font-size: 12px;

  color: #fecaca;

  background: rgba(239, 68, 68, 0.15);

  border: 1px solid rgba(239, 68, 68, 0.25);

  padding: 10px 12px;

  border-radius: 12px;

}



.btn-full {

  width: 100%;

  justify-content: center;

  font-weight: 700;

  border-radius: 12px;

}



.foot-hint {

  margin: 14px 0 0;

  font-size: 11px;

  color: rgba(229, 231, 235, 0.55);

  line-height: 1.45;

}

</style>

