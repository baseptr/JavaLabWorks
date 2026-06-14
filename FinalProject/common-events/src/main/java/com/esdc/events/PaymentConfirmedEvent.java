package com.esdc.events;

import java.time.Instant;

public record PaymentConfirmedEvent(
        String eventId,
        Long bookingId,
        Instant occurredAt
) implements PaymentEvent {
}
