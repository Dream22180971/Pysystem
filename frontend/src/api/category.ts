import { http } from './http'
import { normalizePageResult, unwrap, type PageResult } from './result'

export type Category = {
  categoryId?: number
  categoryName: string
  status: number
}

export async function listCategories(params?: {
  page?: number
  size?: number
  sortField?: string
  sortOrder?: 'asc' | 'desc'
}) {
  const data = await unwrap<unknown>(http.get('/category/list', { params }))
  return normalizePageResult<Category>(data) as PageResult<Category>
}

export async function addCategory(body: Category) {
  return unwrap<string>(http.post('/category/add', body))
}

export async function updateCategory(body: Category) {
  return unwrap<string>(http.post('/category/update', body))
}

export async function deleteCategory(categoryId: number) {
  return unwrap<string>(http.get('/category/delete', { params: { categoryId } }))
}
