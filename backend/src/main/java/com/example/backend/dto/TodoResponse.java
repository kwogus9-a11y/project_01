package com.example.backend.dto;

import com.example.backend.domain.Todo;

public record TodoResponse(Long id, String title, boolean completed) {

	public static TodoResponse from(Todo todo) {
		return new TodoResponse(todo.getId(), todo.getTitle(), todo.isCompleted());
	}

}
