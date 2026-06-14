package com.esdc.booking.saga;

import com.esdc.booking.config.RabbitConfig;
import com.esdc.events.PaymentConfirmedEvent;
import com.esdc.events.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.SAGA_QUEUE)
@RequiredArgsConstructor
public class BookingSagaListener {

    private final BookingSagaService sagaService;

    @RabbitHandler
    public void on(PaymentConfirmedEvent event) {
        sagaService.confirm(event.bookingId());
    }

    @RabbitHandler
    public void on(PaymentFailedEvent event) {
        sagaService.cancel(event.bookingId(), event.reason());
    }
}
