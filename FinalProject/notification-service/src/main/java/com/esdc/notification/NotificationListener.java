package com.esdc.notification;

import com.esdc.events.BookingCancelledEvent;
import com.esdc.events.BookingConfirmedEvent;
import com.esdc.events.BookingExpiredEvent;
import com.esdc.events.SeatReservedEvent;
import com.esdc.notification.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
@RequiredArgsConstructor
public class NotificationListener {

    private final TelegramNotifier notifier;

    @RabbitHandler
    public void on(SeatReservedEvent e) {
        notifier.notify(e.chatId(),
                "Бронь №%d: место удержано, ожидает оплаты".formatted(e.bookingId()));
    }

    @RabbitHandler
    public void on(BookingConfirmedEvent e) {
        notifier.notify(e.chatId(),
                "Бронь №%d подтверждена. Хорошего просмотра!".formatted(e.bookingId()));
    }

    @RabbitHandler
    public void on(BookingCancelledEvent e) {
        notifier.notify(e.chatId(),
                "Бронь №%d отменена: %s".formatted(e.bookingId(), e.reason()));
    }

    @RabbitHandler
    public void on(BookingExpiredEvent e) {
        notifier.notify(e.chatId(),
                "Бронь №%d истекла — место снова свободно".formatted(e.bookingId()));
    }
}
