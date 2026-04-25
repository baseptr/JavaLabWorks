CREATE TABLE IF NOT EXISTS libraries (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(255),
    founded_year INT
);

CREATE TABLE IF NOT EXISTS books (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    author       VARCHAR(255) NOT NULL,
    isbn         VARCHAR(13)  NOT NULL,
    price        NUMERIC(10, 2),
    genre        VARCHAR(100),
    publish_year INT,
    library_id   BIGINT REFERENCES libraries(id) ON DELETE SET NULL,
    UNIQUE (isbn, library_id)
);
