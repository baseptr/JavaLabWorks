package com.esdc.booking.api.dto;

import com.esdc.booking.entity.Booking;
import com.esdc.booking.entity.BookingStatus;

import java.time.Instant;

public record BookingResponse(
        Long id,
        Long showId,
        Long seatId,
        Long userId,
        BookingStatus status,
        Instant heldUntil,
        Instant createdAt
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getShow().getId(),
                b.getSeat().getId(),
                b.getUser().getId(),
                b.getStatus(),
                b.getHeldUntil(),
                b.getCreatedAt());
    }
}
