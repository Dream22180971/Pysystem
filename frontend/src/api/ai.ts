import { http } from './http'
import type { ApiBody } from './result'
import { unwrap } from './result'

export type Citation = {
  path: string
  title: string
}

export type AiChatResponse = {
  assistantName: string
  reply: string
  citations: Citation[]
  usedModel: boolean
  clarifyingQuestions: string[]
}

export async function chat(payload: { message: string; topK?: number }) {
  return unwrap<AiChatResponse>(http.post<ApiBody<AiChatResponse>>('/ai/chat', payload))
}

