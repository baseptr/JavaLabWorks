package com.esdc.audit;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "audit_log", schema = "audit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true, length = 64)
    private String eventId;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "routing_key", nullable = false, length = 64)
    private String routingKey;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String payload;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    public static AuditRecord of(String eventId, String eventType, String routingKey, String payload) {
        AuditRecord r = new AuditRecord();
        r.eventId = eventId;
        r.eventType = eventType;
        r.routingKey = routingKey;
        r.payload = payload;
        r.receivedAt = Instant.now();
        return r;
    }
}
