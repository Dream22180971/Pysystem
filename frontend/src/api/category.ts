import { http } from './http'
import { unwrap } from './result'

export type Category = {
  categoryId?: number
  categoryName: string
  status: number
}

export async function listCategories() {
  return unwrap<Category[]>(http.get('/category/list'))
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
