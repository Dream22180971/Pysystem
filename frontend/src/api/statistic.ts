import { http } from './http'
import { unwrap } from './result'

export type PieItem = { name: string; value: number }
export type BarItem = { name: string; value: number }

export async function getSalePie() {
  return unwrap<PieItem[]>(http.get('/statistic/getSalePie'))
}

export async function getPurchaseBar() {
  return unwrap<BarItem[]>(http.get('/statistic/getPurchaseBar'))
}
