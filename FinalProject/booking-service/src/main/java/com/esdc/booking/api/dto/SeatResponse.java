package com.esdc.booking.api.dto;

import com.esdc.booking.entity.Seat;

public record SeatResponse(
        Long id,
        String rowLabel,
        Integer seatNumber,
        boolean free
) {
    public static SeatResponse from(Seat s, boolean free) {
        return new SeatResponse(s.getId(), s.getRowLabel(), s.getSeatNumber(), free);
    }
}
