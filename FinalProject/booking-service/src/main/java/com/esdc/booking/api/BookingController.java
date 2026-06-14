package com.esdc.booking.api;

import com.esdc.booking.api.dto.BookingRequest;
import com.esdc.booking.api.dto.BookingResponse;
import com.esdc.booking.entity.Booking;
import com.esdc.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody BookingRequest request, Authentication auth) {
        Booking booking = bookingService.reserve(request.showId(), request.seatId(), auth.getName());
        return BookingResponse.from(booking);
    }

    @GetMapping("/{id}")
    public BookingResponse get(@PathVariable Long id) {
        return BookingResponse.from(bookingService.get(id));
    }

    @GetMapping("/my")
    public List<BookingResponse> my(Authentication auth) {
        return bookingService.myBookings(auth.getName()).stream()
                .map(BookingResponse::from)
                .toList();
    }
}
