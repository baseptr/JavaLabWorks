package dto;

import annotation.ValidIsbn;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BookRequest(@NotBlank String title,
                          @NotBlank String author,
                          @ValidIsbn String  isbn,
                          @Min(1) Double price,
                          @NotBlank String genre,
                          @Max(2026) @Min(1000) int publishYear) {
}
