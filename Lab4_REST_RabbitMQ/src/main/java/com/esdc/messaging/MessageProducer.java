package com.esdc.messaging;

import com.esdc.config.RabbitConfig;
import com.esdc.dto.OutgoingMessage;
import com.esdc.dto.TelegramUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(TelegramUpdate update) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, update);
        log.info("Sent message into RabbitMQ, id of message {}", update.updateId());
    }

    public void sendOutgoing(OutgoingMessage message) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.SENT_ROUTING_KEY, message);
        log.info("Sent outgoing event to queue, chatId={}", message.chatId());
    }
}
