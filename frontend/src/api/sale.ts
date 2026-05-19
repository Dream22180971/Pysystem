import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type Sale = {
  saleId?: number
  drugsName: string
  price: number
  num: number
  total: string
  saledate: string
  marks?: string
}

export async function listSales(params?: { page?: number; size?: number; sortField?: string; sortOrder?: 'asc' | 'desc' }) {
  const data = await unwrap<unknown>(http.get('/sale/list', { params }))
  return normalizePageResult<Sale>(data) as PageResult<Sale>
}

export async function addSale(body: Sale) {
  return unwrap<string>(http.post('/sale/add', body))
}

export async function updateSale(body: Sale) {
  return unwrap<string>(http.post('/sale/update', body))
}

export async function deleteSale(saleId: number) {
  return unwrap<string>(http.get('/sale/delete', { params: { saleId } }))
}
