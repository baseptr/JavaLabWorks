package com.esdc.audit;

import com.esdc.audit.config.RabbitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditListener {

    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.AUDIT_QUEUE)
    @Transactional
    public void onEvent(Message message) throws Exception {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String typeId = (String) message.getMessageProperties().getHeaders().get("__TypeId__");
        String eventType = typeId == null ? "unknown" : typeId.substring(typeId.lastIndexOf('.') + 1);
        String eventId = objectMapper.readTree(payload).path("eventId").asText(null);
        if (eventId == null) {
            eventId = UUID.randomUUID().toString();
        }

        int inserted = auditRepository.insertIfAbsent(eventId, eventType, routingKey, payload);
        if (inserted == 0) {
            log.info("audit: duplicate {} [{}] skipped", eventType, eventId);
        } else {
            log.info("audit: stored {} [{}] rk={}", eventType, eventId, routingKey);
        }
    }
}
