CREATE TABLE users (
    id               BIGSERIAL    PRIMARY KEY,
    username         VARCHAR(255) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    role             VARCHAR(16)  NOT NULL,
    telegram_chat_id BIGINT
);

CREATE TABLE shows (
    id        BIGSERIAL    PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    starts_at TIMESTAMPTZ  NOT NULL,
    venue     VARCHAR(255)
);

CREATE TABLE seats (
    id          BIGSERIAL  PRIMARY KEY,
    show_id     BIGINT     NOT NULL REFERENCES shows (id),
    row_label   VARCHAR(8) NOT NULL,
    seat_number INTEGER    NOT NULL,
    CONSTRAINT uq_seat_per_show UNIQUE (show_id, row_label, seat_number)
);

CREATE TABLE bookings (
    id         BIGSERIAL   PRIMARY KEY,
    show_id    BIGINT      NOT NULL REFERENCES shows (id),
    seat_id    BIGINT      NOT NULL REFERENCES seats (id),
    user_id    BIGINT      NOT NULL REFERENCES users (id),
    status     VARCHAR(16) NOT NULL,
    held_until TIMESTAMPTZ,
    version    BIGINT      NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_active_seat ON bookings (show_id, seat_id)
    WHERE status IN ('HELD', 'CONFIRMED');

CREATE INDEX idx_booking_user ON bookings (user_id);
CREATE INDEX idx_booking_status_held ON bookings (status, held_until);

CREATE TABLE outbox (
    id           BIGSERIAL   PRIMARY KEY,
    aggregate_id BIGINT      NOT NULL,
    event_type   VARCHAR(64) NOT NULL,
    routing_key  VARCHAR(64) NOT NULL,
    payload      JSONB       NOT NULL,
    status       VARCHAR(16) NOT NULL DEFAULT 'NEW',
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    processed_at TIMESTAMPTZ
);

CREATE INDEX idx_outbox_unsent ON outbox (id) WHERE status = 'NEW';
