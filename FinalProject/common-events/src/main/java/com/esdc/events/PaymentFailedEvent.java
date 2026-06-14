package com.esdc.events;

import java.time.Instant;

public record PaymentFailedEvent(
        String eventId,
        Long bookingId,
        String reason,
        Instant occurredAt
) implements PaymentEvent {
}
