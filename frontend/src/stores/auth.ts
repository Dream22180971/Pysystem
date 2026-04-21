import { defineStore } from 'pinia'

export type Role = 'ROLE_ADMIN' | 'ROLE_EMP' | 'ROLE_USER'

type AuthState = {
  token: string | null
  username: string | null
  role: Role | null
  /** 与后端 userinfo.P_id 一致，用于展示与登录时角色校验 */
  pId: number | null
}

const STORAGE_KEY = 'pysystem.auth'

function loadState(): AuthState {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return { token: null, username: null, role: null, pId: null }
    const parsed = JSON.parse(raw) as Partial<AuthState>
    return {
      token: typeof parsed.token === 'string' ? parsed.token : null,
      username: typeof parsed.username === 'string' ? parsed.username : null,
      role: (parsed.role as Role) ?? null,
      pId: typeof parsed.pId === 'number' && Number.isFinite(parsed.pId) ? parsed.pId : null,
    }
  } catch {
    return { token: null, username: null, role: null, pId: null }
  }
}

function saveState(state: AuthState) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => loadState(),
  getters: {
    isAuthed: (s) => Boolean(s.token),
  },
  actions: {
    setAuth(payload: { token: string; username: string; role: Role; pId?: number | null }) {
      this.token = payload.token
      this.username = payload.username
      this.role = payload.role
      this.pId = payload.pId ?? null
      saveState({
        token: this.token,
        username: this.username,
        role: this.role,
        pId: this.pId,
      })
    },
    clear() {
      this.token = null
      this.username = null
      this.role = null
      this.pId = null
      saveState({ token: null, username: null, role: null, pId: null })
    },
  },
})

