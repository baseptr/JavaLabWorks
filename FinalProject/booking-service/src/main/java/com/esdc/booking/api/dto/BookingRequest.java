package com.esdc.booking.api.dto;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull Long showId,
        @NotNull Long seatId
) {
}
