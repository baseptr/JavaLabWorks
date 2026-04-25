package com.esdc.dto;

import com.esdc.annotation.ValidIsbn;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record BookRequest(String title,
                          String author,
                          @ValidIsbn String isbn,
                          @Min(1) Double price,
                          String genre,
                          @Max(2026) @Min(1000) Integer publishYear,
                          Long libraryId) {
}
