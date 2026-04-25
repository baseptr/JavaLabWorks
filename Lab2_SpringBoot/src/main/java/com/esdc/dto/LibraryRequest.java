package com.esdc.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LibraryRequest(@NotBlank String name,
                             @NotBlank String address,
                             @Min(1000) @Max(2026) int foundedYear) {
}
