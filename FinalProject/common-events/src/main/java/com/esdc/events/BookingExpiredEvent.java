package com.esdc.events;

import java.time.Instant;

public record BookingExpiredEvent(
        String eventId,
        Long bookingId,
        Long showId,
        Long seatId,
        Long userId,
        String chatId,
        Instant occurredAt
) implements BookingEvent {
}
