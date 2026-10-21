import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, describe, expect, it, vi } from 'vitest'
import App from './App'

function jsonResponse(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' },
  })
}

describe('App', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('불러온 할 일 목록을 보여준다', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        jsonResponse([{ id: 1, title: '공부하기', completed: false }]),
      ),
    )

    render(<App />)

    expect(await screen.findByText('공부하기')).toBeInTheDocument()
    expect(screen.getByText('전체 1개 · 남은 일 1개')).toBeInTheDocument()
  })

  it('목록이 비어 있으면 안내 문구를 보여준다', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(jsonResponse([])))

    render(<App />)

    expect(
      await screen.findByText('등록된 할 일이 없습니다'),
    ).toBeInTheDocument()
  })

  it('새 할 일을 추가한다', async () => {
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce(jsonResponse([]))
      .mockResolvedValueOnce(
        jsonResponse({ id: 1, title: '운동하기', completed: false }, 201),
      )
    vi.stubGlobal('fetch', fetchMock)
    const user = userEvent.setup()

    render(<App />)
    await screen.findByText('등록된 할 일이 없습니다')

    await user.type(screen.getByLabelText('할 일'), '운동하기')
    await user.click(screen.getByRole('button', { name: '추가' }))

    expect(await screen.findByText('운동하기')).toBeInTheDocument()
    expect(fetchMock).toHaveBeenLastCalledWith(
      '/api/todos',
      expect.objectContaining({
        method: 'POST',
        body: JSON.stringify({ title: '운동하기' }),
      }),
    )
  })

  it('API 오류 메시지를 보여준다', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        jsonResponse({ status: 500, message: '서버 오류' }, 500),
      ),
    )

    render(<App />)

    expect(await screen.findByRole('alert')).toHaveTextContent('서버 오류')
  })
})
