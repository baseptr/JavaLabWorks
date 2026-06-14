CREATE TABLE audit_log (
    id          BIGSERIAL    PRIMARY KEY,
    event_id    VARCHAR(64)  NOT NULL UNIQUE,
    event_type  VARCHAR(64)  NOT NULL,
    routing_key VARCHAR(64)  NOT NULL,
    payload     JSONB        NOT NULL,
    received_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);
