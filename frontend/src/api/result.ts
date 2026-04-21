import type { AxiosResponse } from 'axios'

/** 与后端 ResultJson 对齐 */
export type ApiBody<T> = { code: number; message: string; data: T }

/** 解包 axios 响应：code !== 200 时抛错，供页面 catch 提示 */
export async function unwrap<T>(p: Promise<AxiosResponse<ApiBody<T>>>): Promise<T> {
  const res = await p
  const b = res.data
  if (b.code !== 200) throw new Error(b.message || '请求失败')
  return b.data as T
}
