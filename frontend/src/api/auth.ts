import { http } from './http'
import type { Role } from '../stores/auth'

export type LoginResponse = {
  code: number
  message: string
  data: null | {
    token: string
    tokenType: 'Bearer'
    username: string
    role: Role
    pId?: number
  }
}

export async function loginApi(payload: {
  username: string
  password: string
  /** 登录页「快速选择角色」与账号 P_id 一致时通过，否则后端返回 403 */
  expectedPId?: number
}) {
  const res = await http.post<LoginResponse>('/auth/login', payload)
  return res.data
}

