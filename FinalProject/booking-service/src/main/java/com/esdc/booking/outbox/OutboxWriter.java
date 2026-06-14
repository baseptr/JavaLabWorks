package com.esdc.booking.outbox;

import com.esdc.events.BookingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxWriter {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void append(Long aggregateId, String routingKey, BookingEvent event) {
        try {
            outboxRepository.save(OutboxMessage.of(
                    aggregateId,
                    event.getClass().getSimpleName(),
                    routingKey,
                    objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize " + event.getClass().getSimpleName(), e);
        }
    }
}
