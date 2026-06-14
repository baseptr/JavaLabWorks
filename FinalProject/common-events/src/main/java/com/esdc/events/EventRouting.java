package com.esdc.events;

public final class EventRouting {

    private EventRouting() {
    }

    public static final String EXCHANGE = "booking.events";

    public static final String RK_RESERVED = "booking.reserved";
    public static final String RK_CONFIRMED = "booking.confirmed";
    public static final String RK_CANCELLED = "booking.cancelled";
    public static final String RK_EXPIRED = "booking.expired";

    public static final String RK_PAYMENT_CONFIRMED = "payment.confirmed";
    public static final String RK_PAYMENT_FAILED = "payment.failed";

    public static final String PATTERN_ALL = "booking.#";
    public static final String PATTERN_PAYMENT = "payment.#";
}
