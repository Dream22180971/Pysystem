import { http } from './http'
import { unwrap } from './result'

export type Sale = {
  saleId?: number
  drugsName: string
  price: number
  num: number
  total: string
  saledate: string
  marks?: string
}

export async function listSales() {
  return unwrap<Sale[]>(http.get('/sale/list'))
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
