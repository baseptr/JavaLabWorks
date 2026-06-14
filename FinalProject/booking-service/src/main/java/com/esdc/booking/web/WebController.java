package com.esdc.booking.web;

import com.esdc.booking.api.dto.SeatResponse;
import com.esdc.booking.api.dto.ShowRequest;
import com.esdc.booking.entity.Booking;
import com.esdc.booking.exception.SeatAlreadyBookedException;
import com.esdc.booking.service.BookingService;
import com.esdc.booking.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ShowService showService;
    private final BookingService bookingService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/shows";
    }

    @GetMapping("/shows")
    public String shows(Model model, Authentication auth) {
        model.addAttribute("shows", showService.list());
        model.addAttribute("isAdmin", auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        return "shows";
    }

    @PostMapping("/shows")
    public String createShow(@RequestParam String title,
                             @RequestParam String startsAt,
                             @RequestParam(required = false) String venue,
                             @RequestParam int rows,
                             @RequestParam int seatsPerRow) {
        var instant = LocalDateTime.parse(startsAt).atZone(ZoneId.systemDefault()).toInstant();
        showService.create(new ShowRequest(title, instant, venue, rows, seatsPerRow));
        return "redirect:/shows";
    }

    @GetMapping("/shows/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("show", showService.get(id));
        Map<String, java.util.List<SeatResponse>> rows = showService.seats(id).stream()
                .collect(Collectors.groupingBy(SeatResponse::rowLabel,
                        LinkedHashMap::new, Collectors.toList()));
        model.addAttribute("rows", rows);
        return "show";
    }

    @PostMapping("/shows/{id}/book")
    public String book(@PathVariable Long id,
                       @RequestParam Long seatId,
                       Authentication auth,
                       RedirectAttributes redirect) {
        try {
            Booking booking = bookingService.reserve(id, seatId, auth.getName());
            redirect.addFlashAttribute("message",
                    "Место удержано, бронь №" + booking.getId() + " — ждём оплату");
        } catch (SeatAlreadyBookedException e) {
            redirect.addFlashAttribute("error", "Место уже занято");
        }
        return "redirect:/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Model model, Authentication auth) {
        model.addAttribute("bookings", bookingService.myBookingViews(auth.getName()));
        return "my-bookings";
    }
}
