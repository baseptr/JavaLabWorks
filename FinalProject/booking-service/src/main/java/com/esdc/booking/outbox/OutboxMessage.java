package com.esdc.booking.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OutboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_id", nullable = false, updatable = false)
    private Long aggregateId;

    @Column(name = "event_type", nullable = false, updatable = false, length = 64)
    private String eventType;

    @Column(name = "routing_key", nullable = false, updatable = false, length = 64)
    private String routingKey;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, updatable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private OutboxStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    public static OutboxMessage of(Long aggregateId, String eventType, String routingKey, String payload) {
        OutboxMessage m = new OutboxMessage();
        m.aggregateId = aggregateId;
        m.eventType = eventType;
        m.routingKey = routingKey;
        m.payload = payload;
        m.status = OutboxStatus.NEW;
        m.createdAt = Instant.now();
        return m;
    }

    public void markSent() {
        this.status = OutboxStatus.SENT;
        this.processedAt = Instant.now();
    }
}
