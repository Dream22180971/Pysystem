import { http } from './http'
import { unwrap } from './result'

export type Kcxx = {
  kid?: number
  drugsName: string
  num: number
  rid: number
  marks?: string
}

export async function listKcxx() {
  return unwrap<Kcxx[]>(http.get('/kcxx/list'))
}

export async function listWarning() {
  return unwrap<Kcxx[]>(http.get('/kcxx/getWarningList'))
}

export async function addKcxx(body: Kcxx) {
  return unwrap<string>(http.post('/kcxx/add', body))
}

export async function updateKcxx(body: Kcxx) {
  return unwrap<string>(http.post('/kcxx/update', body))
}

export async function deleteKcxx(kid: number) {
  return unwrap<string>(http.get('/kcxx/delete', { params: { kid } }))
}
