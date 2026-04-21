import { http } from './http'
import { unwrap } from './result'

export type Drugs = {
  id?: number
  drugsName: string
  nums: number
  drugsImage: string
  categoryId: number
  price: number
  people: string
  useMethod: string
  cid: number
  rid: number
  productTime: string
  saveTime: number
  status: number
}

export async function listDrugs() {
  return unwrap<Drugs[]>(http.get('/drugs/list'))
}

export async function addDrug(body: Drugs) {
  return unwrap<string>(http.post('/drugs/add', body))
}

export async function updateDrug(body: Drugs) {
  return unwrap<string>(http.post('/drugs/update', body))
}

export async function deleteDrug(id: number) {
  return unwrap<string>(http.get('/drugs/delete', { params: { id } }))
}
