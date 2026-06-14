package com.esdc.booking.saga;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HoldExpirationScheduler {

    private final BookingSagaService sagaService;

    @Scheduled(fixedDelay = 30_000)
    public void expireStaleHolds() {
        sagaService.expireStaleHolds();
    }
}
