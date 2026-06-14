package com.esdc.booking.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ShowRequest(
        @NotBlank String title,
        @NotNull Instant startsAt,
        String venue,
        @Min(1) @Max(26) int rows,
        @Min(1) @Max(100) int seatsPerRow
) {
}
