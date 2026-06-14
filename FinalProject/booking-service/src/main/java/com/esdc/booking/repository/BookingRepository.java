package com.esdc.booking.repository;

import com.esdc.booking.entity.Booking;
import com.esdc.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatusAndHeldUntilBefore(BookingStatus status, Instant time);

    List<Booking> findByShowIdAndStatusIn(Long showId, Collection<BookingStatus> statuses);
}
