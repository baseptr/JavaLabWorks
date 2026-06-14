package com.esdc.booking.web.dto;

import com.esdc.booking.entity.Booking;
import com.esdc.booking.entity.BookingStatus;

import java.time.Instant;

public record BookingView(
        Long id,
        String showTitle,
        String seatLabel,
        BookingStatus status,
        Instant heldUntil,
        Instant createdAt
) {
    public static BookingView from(Booking b) {
        return new BookingView(
                b.getId(),
                b.getShow().getTitle(),
                b.getSeat().getRowLabel() + b.getSeat().getSeatNumber(),
                b.getStatus(),
                b.getHeldUntil(),
                b.getCreatedAt());
    }
}
