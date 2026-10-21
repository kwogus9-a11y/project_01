package com.example.backend.controller;

import com.example.backend.dto.TodoCreateRequest;
import com.example.backend.dto.TodoResponse;
import com.example.backend.dto.TodoUpdateRequest;
import com.example.backend.service.TodoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	private final TodoService todoService;

	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping
	public List<TodoResponse> findAll() {
		return todoService.findAll();
	}

	@GetMapping("/{id}")
	public TodoResponse findById(@PathVariable Long id) {
		return todoService.findById(id);
	}

	@PostMapping
	public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoCreateRequest request) {
		TodoResponse response = todoService.create(request);
		return ResponseEntity.created(URI.create("/api/todos/" + response.id())).body(response);
	}

	@PutMapping("/{id}")
	public TodoResponse update(@PathVariable Long id, @Valid @RequestBody TodoUpdateRequest request) {
		return todoService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		todoService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
