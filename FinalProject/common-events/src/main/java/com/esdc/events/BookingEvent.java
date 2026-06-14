package com.esdc.events;

import java.time.Instant;

public sealed interface BookingEvent
        permits SeatReservedEvent, BookingConfirmedEvent, BookingCancelledEvent, BookingExpiredEvent {

    String eventId();

    Long bookingId();

    Instant occurredAt();
}
