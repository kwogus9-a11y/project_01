package com.example.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.backend.domain.Todo;
import com.example.backend.dto.TodoCreateRequest;
import com.example.backend.dto.TodoResponse;
import com.example.backend.dto.TodoUpdateRequest;
import com.example.backend.exception.TodoNotFoundException;
import com.example.backend.repository.TodoRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

	@Mock
	private TodoRepository todoRepository;

	@InjectMocks
	private TodoService todoService;

	@Test
	void create_savesTodoAndReturnsResponse() {
		given(todoRepository.save(any(Todo.class))).willAnswer(invocation -> invocation.getArgument(0));

		TodoResponse response = todoService.create(new TodoCreateRequest("공부하기"));

		assertThat(response.title()).isEqualTo("공부하기");
		assertThat(response.completed()).isFalse();
		verify(todoRepository).save(any(Todo.class));
	}

	@Test
	void update_changesTitleAndCompleted() {
		Todo todo = new Todo("before");
		given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

		TodoResponse response = todoService.update(1L, new TodoUpdateRequest("after", true));

		assertThat(response.title()).isEqualTo("after");
		assertThat(response.completed()).isTrue();
	}

	@Test
	void findById_throwsWhenMissing() {
		given(todoRepository.findById(99L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> todoService.findById(99L))
				.isInstanceOf(TodoNotFoundException.class);
	}

}
