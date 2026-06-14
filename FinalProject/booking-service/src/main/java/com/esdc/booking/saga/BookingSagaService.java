package com.esdc.booking.saga;

import com.esdc.booking.entity.Booking;
import com.esdc.booking.entity.BookingStatus;
import com.esdc.booking.outbox.OutboxWriter;
import com.esdc.booking.repository.BookingRepository;
import com.esdc.booking.service.BookingService;
import com.esdc.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingSagaService {

    private final BookingRepository bookingRepository;
    private final OutboxWriter outboxWriter;

    @Transactional
    public void confirm(Long bookingId) {
        Booking b = find(bookingId, BookingStatus.CONFIRMED);
        if (b == null) {
            return;
        }
        b.confirm();
        outboxWriter.append(b.getId(), EventRouting.RK_CONFIRMED, new BookingConfirmedEvent(
                UUID.randomUUID().toString(), b.getId(), b.getShow().getId(), b.getSeat().getId(),
                b.getUser().getId(), BookingService.chatId(b.getUser()), Instant.now()));
        log.info("saga: booking {} CONFIRMED", bookingId);
    }

    @Transactional
    public void cancel(Long bookingId, String reason) {
        Booking b = find(bookingId, BookingStatus.CANCELLED);
        if (b == null) {
            return;
        }
        b.cancel();
        outboxWriter.append(b.getId(), EventRouting.RK_CANCELLED, new BookingCancelledEvent(
                UUID.randomUUID().toString(), b.getId(), b.getShow().getId(), b.getSeat().getId(),
                b.getUser().getId(), BookingService.chatId(b.getUser()), reason, Instant.now()));
        log.info("saga: booking {} CANCELLED ({})", bookingId, reason);
    }

    @Transactional
    public void expireStaleHolds() {
        List<Booking> stale = bookingRepository
                .findByStatusAndHeldUntilBefore(BookingStatus.HELD, Instant.now());
        for (Booking b : stale) {
            b.expire();
            outboxWriter.append(b.getId(), EventRouting.RK_EXPIRED, new BookingExpiredEvent(
                    UUID.randomUUID().toString(), b.getId(), b.getShow().getId(), b.getSeat().getId(),
                    b.getUser().getId(), BookingService.chatId(b.getUser()), Instant.now()));
            log.info("saga: booking {} EXPIRED", b.getId());
        }
    }

    private Booking find(Long bookingId, BookingStatus target) {
        Booking b = bookingRepository.findById(bookingId).orElse(null);
        if (b == null) {
            log.warn("saga: booking {} not found, event ignored", bookingId);
            return null;
        }
        if (b.getStatus() == target) {
            return null;
        }
        if (b.getStatus() != BookingStatus.HELD) {
            log.warn("saga: booking {} is {}, transition to {} ignored", bookingId, b.getStatus(), target);
            return null;
        }
        return b;
    }
}
