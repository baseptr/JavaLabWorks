-- ============================================================
--  Coffee Stats DB – init script
-- ============================================================

-- Users
CREATE TABLE IF NOT EXISTS users (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Coffee / beverage catalogue
CREATE TABLE IF NOT EXISTS coffee (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(100)   NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

-- Orders
CREATE TABLE IF NOT EXISTS orders (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT         NOT NULL REFERENCES users (id),
    total_price NUMERIC(10, 2) NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Order items  (many-to-many: order <-> coffee)
CREATE TABLE IF NOT EXISTS order_items (
    order_id  BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    coffee_id BIGINT NOT NULL REFERENCES coffee (id),
    PRIMARY KEY (order_id, coffee_id)
);

-- ============================================================
--  Seed data – basic beverages
-- ============================================================
INSERT INTO coffee (name, price) VALUES
    ('Espresso',        2.50),
    ('Americano',       3.00),
    ('Cappuccino',      3.50),
    ('Latte',           4.00),
    ('Flat White',      4.00),
    ('Macchiato',       3.75),
    ('Mocha',           4.50),
    ('Cold Brew',       4.25),
    ('Hot Chocolate',   3.50),
    ('Chai Latte',      3.75)
ON CONFLICT DO NOTHING;

