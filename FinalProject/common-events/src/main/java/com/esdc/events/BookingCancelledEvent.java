package com.esdc.events;

import java.time.Instant;

public record BookingCancelledEvent(
        String eventId,
        Long bookingId,
        Long showId,
        Long seatId,
        Long userId,
        String chatId,
        String reason,
        Instant occurredAt
) implements BookingEvent {
}
