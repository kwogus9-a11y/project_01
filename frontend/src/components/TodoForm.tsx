import { useState, type FormEvent } from 'react'

interface TodoFormProps {
  onSubmit: (title: string) => Promise<void>
}

function TodoForm({ onSubmit }: TodoFormProps) {
  const [title, setTitle] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const trimmed = title.trim()
    if (!trimmed) return

    setSubmitting(true)
    try {
      await onSubmit(trimmed)
      setTitle('')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className="todo-form" onSubmit={handleSubmit}>
      <input
        type="text"
        value={title}
        onChange={(event) => setTitle(event.target.value)}
        placeholder="할 일을 입력하세요"
        maxLength={100}
        aria-label="할 일"
      />
      <button type="submit" disabled={submitting || !title.trim()}>
        추가
      </button>
    </form>
  )
}

export default TodoForm
