package com.esdc.booking.service;

import com.esdc.booking.api.dto.SeatResponse;
import com.esdc.booking.api.dto.ShowRequest;
import com.esdc.booking.entity.Booking;
import com.esdc.booking.entity.BookingStatus;
import com.esdc.booking.entity.Seat;
import com.esdc.booking.entity.Show;
import com.esdc.booking.exception.NotFoundException;
import com.esdc.booking.repository.BookingRepository;
import com.esdc.booking.repository.SeatRepository;
import com.esdc.booking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Show create(ShowRequest request) {
        Show show = showRepository.save(Show.builder()
                .title(request.title())
                .startsAt(request.startsAt())
                .venue(request.venue())
                .build());
        List<Seat> seats = new ArrayList<>();
        for (int row = 0; row < request.rows(); row++) {
            String rowLabel = String.valueOf((char) ('A' + row));
            for (int n = 1; n <= request.seatsPerRow(); n++) {
                seats.add(Seat.builder().show(show).rowLabel(rowLabel).seatNumber(n).build());
            }
        }
        seatRepository.saveAll(seats);
        return show;
    }

    @Transactional(readOnly = true)
    public List<Show> list() {
        return showRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Show get(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Show " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> seats(Long showId) {
        get(showId);
        Set<Long> taken = bookingRepository
                .findByShowIdAndStatusIn(showId, List.of(BookingStatus.HELD, BookingStatus.CONFIRMED))
                .stream()
                .map(b -> b.getSeat().getId())
                .collect(Collectors.toSet());
        return seatRepository.findByShowId(showId).stream()
                .sorted(Comparator.comparing(Seat::getRowLabel).thenComparing(Seat::getSeatNumber))
                .map(s -> SeatResponse.from(s, !taken.contains(s.getId())))
                .toList();
    }
}
