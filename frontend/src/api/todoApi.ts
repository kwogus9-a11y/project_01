import type { Todo, TodoCreateRequest, TodoUpdateRequest } from '../types/todo'

const BASE_URL = '/api/todos'

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  })

  if (!response.ok) {
    const error = await response.json().catch(() => null)
    throw new Error(error?.message ?? `요청 실패 (${response.status})`)
  }

  if (response.status === 204) {
    return undefined as T
  }
  return response.json() as Promise<T>
}

export const todoApi = {
  findAll: () => request<Todo[]>(BASE_URL),

  create: (body: TodoCreateRequest) =>
    request<Todo>(BASE_URL, { method: 'POST', body: JSON.stringify(body) }),

  update: (id: number, body: TodoUpdateRequest) =>
    request<Todo>(`${BASE_URL}/${id}`, {
      method: 'PUT',
      body: JSON.stringify(body),
    }),

  remove: (id: number) =>
    request<void>(`${BASE_URL}/${id}`, { method: 'DELETE' }),
}
