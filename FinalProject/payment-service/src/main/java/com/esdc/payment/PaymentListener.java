package com.esdc.payment;

import com.esdc.events.EventRouting;
import com.esdc.events.PaymentConfirmedEvent;
import com.esdc.events.PaymentFailedEvent;
import com.esdc.events.SeatReservedEvent;
import com.esdc.payment.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentListener {

    private static final int DECLINE_EVERY = 5;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void onSeatReserved(SeatReservedEvent event) {
        Long bookingId = event.bookingId();
        if (bookingId % DECLINE_EVERY == 0) {
            log.info("payment: booking {} DECLINED", bookingId);
            rabbitTemplate.convertAndSend(EventRouting.EXCHANGE, EventRouting.RK_PAYMENT_FAILED,
                    new PaymentFailedEvent(UUID.randomUUID().toString(), bookingId,
                            "card declined", Instant.now()));
        } else {
            log.info("payment: booking {} confirmed", bookingId);
            rabbitTemplate.convertAndSend(EventRouting.EXCHANGE, EventRouting.RK_PAYMENT_CONFIRMED,
                    new PaymentConfirmedEvent(UUID.randomUUID().toString(), bookingId, Instant.now()));
        }
    }
}
