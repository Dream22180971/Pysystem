import { defineStore } from 'pinia'

type Role = 'ROLE_ADMIN' | 'ROLE_EMP' | 'ROLE_USER'

type AuthState = {
  token: string | null
  username: string | null
  role: Role | null
}

const STORAGE_KEY = 'pysystem.auth'

function loadState(): AuthState {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return { token: null, username: null, role: null }
    const parsed = JSON.parse(raw) as Partial<AuthState>
    return {
      token: typeof parsed.token === 'string' ? parsed.token : null,
      username: typeof parsed.username === 'string' ? parsed.username : null,
      role: (parsed.role as Role) ?? null,
    }
  } catch {
    return { token: null, username: null, role: null }
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
    setAuth(payload: { token: string; username: string; role: Role }) {
      this.token = payload.token
      this.username = payload.username
      this.role = payload.role
      saveState({ token: this.token, username: this.username, role: this.role })
    },
    clear() {
      this.token = null
      this.username = null
      this.role = null
      saveState({ token: null, username: null, role: null })
    },
  },
})

