package com.example.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.dto.TodoCreateRequest;
import com.example.backend.dto.TodoResponse;
import com.example.backend.exception.TodoNotFoundException;
import com.example.backend.service.TodoService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TodoService todoService;

	@Test
	void findAll_returnsTodos() throws Exception {
		given(todoService.findAll()).willReturn(List.of(new TodoResponse(1L, "공부하기", false)));

		mockMvc.perform(get("/api/todos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].title").value("공부하기"))
				.andExpect(jsonPath("$[0].completed").value(false));
	}

	@Test
	void create_returnsCreated() throws Exception {
		given(todoService.create(any(TodoCreateRequest.class))).willReturn(new TodoResponse(1L, "공부하기", false));

		mockMvc.perform(post("/api/todos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"title\":\"공부하기\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/todos/1"))
				.andExpect(jsonPath("$.title").value("공부하기"));
	}

	@Test
	void create_rejectsBlankTitle() throws Exception {
		mockMvc.perform(post("/api/todos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"title\":\"\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void findById_returnsNotFound() throws Exception {
		given(todoService.findById(99L)).willThrow(new TodoNotFoundException(99L));

		mockMvc.perform(get("/api/todos/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

}
