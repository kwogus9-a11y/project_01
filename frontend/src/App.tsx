import { useEffect, useState } from 'react'
import { todoApi } from './api/todoApi'
import TodoForm from './components/TodoForm'
import TodoItem from './components/TodoItem'
import type { Todo } from './types/todo'

function App() {
  const [todos, setTodos] = useState<Todo[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    todoApi
      .findAll()
      .then(setTodos)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false))
  }, [])

  const handleError = (e: unknown) => {
    setError(e instanceof Error ? e.message : '알 수 없는 오류가 발생했습니다')
  }

  const handleCreate = async (title: string) => {
    try {
      const created = await todoApi.create({ title })
      setTodos((prev) => [...prev, created])
      setError(null)
    } catch (e) {
      handleError(e)
    }
  }

  const handleToggle = async (todo: Todo) => {
    try {
      const updated = await todoApi.update(todo.id, {
        title: todo.title,
        completed: !todo.completed,
      })
      setTodos((prev) => prev.map((t) => (t.id === updated.id ? updated : t)))
    } catch (e) {
      handleError(e)
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await todoApi.remove(id)
      setTodos((prev) => prev.filter((t) => t.id !== id))
    } catch (e) {
      handleError(e)
    }
  }

  const remaining = todos.filter((t) => !t.completed).length

  return (
    <main className="app">
      <header>
        <h1>할 일 목록</h1>
        <p className="summary">
          전체 {todos.length}개 · 남은 일 {remaining}개
        </p>
      </header>

      <TodoForm onSubmit={handleCreate} />

      {error && (
        <p className="error" role="alert">
          {error}
        </p>
      )}

      {loading ? (
        <p className="empty">불러오는 중...</p>
      ) : todos.length === 0 ? (
        <p className="empty">등록된 할 일이 없습니다</p>
      ) : (
        <ul className="todo-list">
          {todos.map((todo) => (
            <TodoItem
              key={todo.id}
              todo={todo}
              onToggle={handleToggle}
              onDelete={handleDelete}
            />
          ))}
        </ul>
      )}
    </main>
  )
}

export default App
