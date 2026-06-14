package com.esdc.booking.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 6) String password
) {
}
