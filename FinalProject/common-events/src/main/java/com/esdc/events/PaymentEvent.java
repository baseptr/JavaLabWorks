package com.esdc.events;

import java.time.Instant;

public sealed interface PaymentEvent permits PaymentConfirmedEvent, PaymentFailedEvent {

    String eventId();

    Long bookingId();

    Instant occurredAt();
}
