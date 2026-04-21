import { http } from './http'
import { unwrap } from './result'

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

export async function listUsers() {
  return unwrap<Userinfo[]>(http.get('/user/list'))
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
