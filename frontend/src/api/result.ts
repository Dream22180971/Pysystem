import type { AxiosResponse } from 'axios'

/** 与后端 ResultJson 对齐 */
export type ApiBody<T> = { code: number; message: string; data: T }

export type PageResult<T> = { items: T[]; total: number }

export function normalizePageResult<T>(data: unknown): PageResult<T> {
  if (data && typeof data === 'object') {
    const d = data as { items?: unknown; total?: unknown }
    if (Array.isArray(d.items)) {
      const total = typeof d.total === 'number' ? d.total : Number(d.total ?? d.items.length) || d.items.length
      return { items: d.items as T[], total }
    }
  }
  if (Array.isArray(data)) {
    return { items: data as T[], total: data.length }
  }
  return { items: [], total: 0 }
}

/** 解包 axios 响应：code !== 200 时抛错，供页面 catch 提示 */
export async function unwrap<T>(p: Promise<AxiosResponse<ApiBody<T>>>): Promise<T> {
  const res = await p
  const b = res.data
  if (b.code !== 200) throw new Error(b.message || '请求失败')
  return b.data as T
}
