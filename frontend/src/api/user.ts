import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type Userinfo = {
  id?: number
  username: string
  password?: string | null
  nickname: string
  sex: string
  age: number
  phone: string
  address: string
  pId: number
  status: number
  createTime?: string
}

export async function listUsers(params?: { page?: number; size?: number; sortField?: string; sortOrder?: 'asc' | 'desc' }) {
  const data = await unwrap<unknown>(http.get('/user/list', { params }))
  return normalizePageResult<Userinfo>(data) as PageResult<Userinfo>
}

export async function addUser(body: Userinfo) {
  return unwrap<string>(http.post('/user/add', body))
}

export async function updateUser(body: Userinfo) {
  return unwrap<string>(http.post('/user/update', body))
}

export async function deleteUser(id: number) {
  return unwrap<string>(http.get('/user/delete', { params: { id } }))
}
