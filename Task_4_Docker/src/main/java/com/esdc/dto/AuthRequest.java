package com.esdc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(@NotBlank String username, @NotBlank @Size(min = 8) String password) {
}
