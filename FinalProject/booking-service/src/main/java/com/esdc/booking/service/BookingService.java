package com.esdc.booking.service;

import com.esdc.booking.entity.*;
import com.esdc.booking.exception.NotFoundException;
import com.esdc.booking.exception.SeatAlreadyBookedException;
import com.esdc.booking.outbox.OutboxWriter;
import com.esdc.booking.repository.BookingRepository;
import com.esdc.booking.repository.SeatRepository;
import com.esdc.booking.repository.UserRepository;
import com.esdc.booking.web.dto.BookingView;
import com.esdc.events.EventRouting;
import com.esdc.events.SeatReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final Duration HOLD_DURATION = Duration.ofMinutes(15);

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final OutboxWriter outboxWriter;

    @Transactional
    public Booking reserve(Long showId, Long seatId, String username) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException("Seat " + seatId + " not found"));
        if (!seat.getShow().getId().equals(showId)) {
            throw new NotFoundException("Seat " + seatId + " does not belong to show " + showId);
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User " + username + " not found"));

        Booking booking = Booking.builder()
                .show(seat.getShow())
                .seat(seat)
                .user(user)
                .status(BookingStatus.HELD)
                .heldUntil(Instant.now().plus(HOLD_DURATION))
                .build();
        try {
            booking = bookingRepository.saveAndFlush(booking);
        } catch (DataIntegrityViolationException e) {
            throw new SeatAlreadyBookedException(showId, seatId);
        }

        outboxWriter.append(booking.getId(), EventRouting.RK_RESERVED, new SeatReservedEvent(
                UUID.randomUUID().toString(),
                booking.getId(),
                showId,
                seatId,
                user.getId(),
                chatId(user),
                Instant.now()));
        return booking;
    }

    @Transactional(readOnly = true)
    public Booking get(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Booking> myBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
        return bookingRepository.findByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public List<BookingView> myBookingViews(String username) {
        return myBookings(username).stream().map(BookingView::from).toList();
    }

    public static String chatId(User user) {
        return user.getTelegramChatId() == null ? null : user.getTelegramChatId().toString();
    }
}
