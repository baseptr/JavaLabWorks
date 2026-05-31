package com.esdc.messaging;

import com.esdc.config.RabbitConfig;
import com.esdc.dto.OutgoingMessage;
import com.esdc.dto.TelegramUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);
    @Value("${spring.rabbitmq.consume-timeout}")
    private int CONSUME_TIMEOUT;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receive(TelegramUpdate update) throws InterruptedException {
        Thread.sleep(CONSUME_TIMEOUT);
        log.info("Received update id={}, sender={}, text={}",
                update.updateId(),
                update.message() != null ? update.message().from().firstName() : "unknown",
                update.message() != null ? update.message().text() : "no text");
    }

    @RabbitListener(queues = RabbitConfig.SENT_QUEUE_NAME)
    public void receiveSent(OutgoingMessage message) throws InterruptedException {
        Thread.sleep(CONSUME_TIMEOUT);
        log.info("Outgoing message processed: chatId={}, text={}", message.chatId(), message.text());
    }
}
