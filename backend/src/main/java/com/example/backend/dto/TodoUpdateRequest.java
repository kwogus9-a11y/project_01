package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequest(
		@NotBlank @Size(max = 100) String title,
		boolean completed
) {
}
