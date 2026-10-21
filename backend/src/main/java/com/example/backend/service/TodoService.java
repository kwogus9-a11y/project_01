package com.example.backend.service;

import com.example.backend.domain.Todo;
import com.example.backend.dto.TodoCreateRequest;
import com.example.backend.dto.TodoResponse;
import com.example.backend.dto.TodoUpdateRequest;
import com.example.backend.exception.TodoNotFoundException;
import com.example.backend.repository.TodoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TodoService {

	private final TodoRepository todoRepository;

	public TodoService(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	public List<TodoResponse> findAll() {
		return todoRepository.findAll().stream()
				.map(TodoResponse::from)
				.toList();
	}

	public TodoResponse findById(Long id) {
		return TodoResponse.from(getTodo(id));
	}

	@Transactional
	public TodoResponse create(TodoCreateRequest request) {
		Todo saved = todoRepository.save(new Todo(request.title()));
		return TodoResponse.from(saved);
	}

	@Transactional
	public TodoResponse update(Long id, TodoUpdateRequest request) {
		Todo todo = getTodo(id);
		todo.update(request.title(), request.completed());
		return TodoResponse.from(todo);
	}

	@Transactional
	public void delete(Long id) {
		todoRepository.delete(getTodo(id));
	}

	private Todo getTodo(Long id) {
		return todoRepository.findById(id)
				.orElseThrow(() -> new TodoNotFoundException(id));
	}

}
