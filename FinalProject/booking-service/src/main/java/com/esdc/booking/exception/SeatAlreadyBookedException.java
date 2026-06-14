package com.esdc.booking.exception;

public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(Long showId, Long seatId) {
        super("Seat " + seatId + " of show " + showId + " is already held or booked");
    }
}
