package com.esdc.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditRepository extends JpaRepository<AuditRecord, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO audit.audit_log (event_id, event_type, routing_key, payload, received_at)
            VALUES (:eventId, :eventType, :routingKey, CAST(:payload AS jsonb), now())
            ON CONFLICT (event_id) DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(@Param("eventId") String eventId,
                       @Param("eventType") String eventType,
                       @Param("routingKey") String routingKey,
                       @Param("payload") String payload);
}
