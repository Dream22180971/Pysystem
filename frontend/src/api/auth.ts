import { http } from './http'

export type LoginResponse = {
  code: number
  message: string
  data: null | {
    token: string
    tokenType: 'Bearer'
    username: string
    role: 'ROLE_ADMIN' | 'ROLE_EMP' | 'ROLE_USER'
  }
}

export async function loginApi(payload: { username: string; password: string }) {
  const res = await http.post<LoginResponse>('/auth/login', payload)
  return res.data
}

