package com.esdc.booking.api.dto;

import com.esdc.booking.entity.Show;

import java.time.Instant;

public record ShowResponse(
        Long id,
        String title,
        Instant startsAt,
        String venue
) {
    public static ShowResponse from(Show s) {
        return new ShowResponse(s.getId(), s.getTitle(), s.getStartsAt(), s.getVenue());
    }
}
