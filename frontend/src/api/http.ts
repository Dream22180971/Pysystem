/**
 * Axios 实例：baseURL 指向 /api（开发时由 Vite 代理到后端 8080）。
 * 请求拦截器附加 Bearer Token；优先读 Pinia，未就绪时回退 localStorage，避免首屏请求 401。
 */
import axios from 'axios'
import { getActivePinia } from 'pinia'
import { useAuthStore } from '../stores/auth'

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE ?? '/api',
  timeout: 15000,
})

/** 与 stores/auth.ts 中 STORAGE_KEY 保持一致；避免 Pinia 尚未激活时取不到 token 导致 401 */
const AUTH_STORAGE_KEY = 'pysystem.auth'

function resolveToken(): string | null {
  try {
    if (getActivePinia()) {
      const auth = useAuthStore()
      if (auth.token) return auth.token
    }
  } catch {
    /* Pinia 未就绪时走 localStorage */
  }
  try {
    const raw = localStorage.getItem(AUTH_STORAGE_KEY)
    if (!raw) return null
    const p = JSON.parse(raw) as { token?: string | null }
    return typeof p.token === 'string' && p.token.length > 0 ? p.token : null
  } catch {
    return null
  }
}

http.interceptors.request.use((config) => {
  const token = resolveToken()
  if (token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (res) => res,
  (error) => {
    // 后端返回 401：通常是 token 过期/后端密钥变更/本地残留无效 token
    const status = error?.response?.status as number | undefined
    if (status === 401) {
      try {
        localStorage.removeItem(AUTH_STORAGE_KEY)
      } catch {
        // ignore
      }
      // 避免循环跳转
      if (typeof window !== 'undefined' && window.location.pathname !== '/login') {
        const redirect = window.location.pathname + window.location.search
        window.location.href = `/login?redirect=${encodeURIComponent(redirect)}`
      }
    }
    return Promise.reject(error)
  }
)

