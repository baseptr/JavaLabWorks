package com.esdc.booking.api;

import com.esdc.booking.api.dto.SeatResponse;
import com.esdc.booking.api.dto.ShowRequest;
import com.esdc.booking.api.dto.ShowResponse;
import com.esdc.booking.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowResponse create(@Valid @RequestBody ShowRequest request) {
        return ShowResponse.from(showService.create(request));
    }

    @GetMapping
    public List<ShowResponse> list() {
        return showService.list().stream().map(ShowResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ShowResponse get(@PathVariable Long id) {
        return ShowResponse.from(showService.get(id));
    }

    @GetMapping("/{id}/seats")
    public List<SeatResponse> seats(@PathVariable Long id) {
        return showService.seats(id);
    }
}
