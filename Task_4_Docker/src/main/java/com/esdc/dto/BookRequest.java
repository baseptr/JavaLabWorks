package com.esdc.dto;

import com.esdc.annotation.ValidIsbn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookRequest(@NotBlank String title,
                          @NotBlank String author,
                          @ValidIsbn @Schema(description = "ISBN-13: 13 цифр, дефисы допустимы", example = "978-0-13-235088-4") @NotNull String isbn,
                          @Min(1) Double price,
                          @NotBlank String genre,
                          @Max(2026) @Min(1000) Integer publishYear,
                          @NotNull @Positive Long libraryId) {
}
