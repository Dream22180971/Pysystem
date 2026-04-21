import { http } from './http'
import { unwrap } from './result'

export type Purchase = {
  pid?: number
  drugsName: string
  num: number
  indate: string
  rid: number
  marks?: string
}

export async function listPurchases() {
  return unwrap<Purchase[]>(http.get('/purchase/list'))
}

export async function addPurchase(body: Purchase) {
  return unwrap<string>(http.post('/purchase/add', body))
}

export async function updatePurchase(body: Purchase) {
  return unwrap<string>(http.post('/purchase/update', body))
}

export async function deletePurchase(pid: number) {
  return unwrap<string>(http.get('/purchase/delete', { params: { pid } }))
}
