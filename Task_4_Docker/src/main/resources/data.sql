-- admin / admin222
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$Pn2n/yGZnSmFtJLbWR/.XOwntF4Q2pFx0TFJKyTGNQQtaglullHum', 'ADMIN')
ON CONFLICT (username) DO NOTHING;
