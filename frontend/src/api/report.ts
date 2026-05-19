import { http } from './http'
import { unwrap } from './result'

export type ReportDrugAgg = { drugsName: string; qty: number; amount: number }
export type ReportDayAgg = { day: string; qty: number; amount: number }

export async function salesByDrug(params?: { start?: string; end?: string; limit?: number }) {
  return unwrap<ReportDrugAgg[]>(http.get('/report/sales/drug', { params }))
}

export async function salesByDay(params?: { start?: string; end?: string }) {
  return unwrap<ReportDayAgg[]>(http.get('/report/sales/day', { params }))
}

export async function purchaseByDrug(params?: { start?: string; end?: string; limit?: number }) {
  return unwrap<ReportDrugAgg[]>(http.get('/report/purchase/drug', { params }))
}

export async function lowStock(params?: { threshold?: number; limit?: number }) {
  return unwrap<ReportDrugAgg[]>(http.get('/report/inventory/low', { params }))
}

