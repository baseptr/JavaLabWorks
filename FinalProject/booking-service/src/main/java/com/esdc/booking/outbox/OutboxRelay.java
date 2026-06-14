package com.esdc.booking.outbox;

import com.esdc.events.EventRouting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {

    private static final String EVENTS_PACKAGE = "com.esdc.events.";
    private static final int BATCH_SIZE = 100;

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishPending() {
        List<OutboxMessage> batch = outboxRepository.lockUnsent(BATCH_SIZE);
        for (OutboxMessage m : batch) {
            Message message = MessageBuilder
                    .withBody(m.getPayload().getBytes(StandardCharsets.UTF_8))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding(StandardCharsets.UTF_8.name())
                    .setHeader("__TypeId__", EVENTS_PACKAGE + m.getEventType())
                    .build();
            rabbitTemplate.send(EventRouting.EXCHANGE, m.getRoutingKey(), message);
            m.markSent();
            log.info("outbox: published {} [{}] for aggregate {}",
                    m.getEventType(), m.getRoutingKey(), m.getAggregateId());
        }
    }
}
