# Makefile для работы с PostgreSQL (Coffee Stats)

DB_PORT := 5437
DB_NAME := coffeedb
DB_USER := coffeeuser
CONTAINER_NAME := cofee-postgres
DOCKER_FILE := src/main/java/com/esdc/lab2/docker-file.yaml
INIT_SQL := src/main/java/com/esdc/lab2/init.sql

.PHONY: help db-up db-down db-restart db-logs db-shell db-import

help:
	@echo "Команды для работы с БД:"
	@echo "  make db-up      - Запустить PostgreSQL в Docker"
	@echo "  make db-down    - Остановить PostgreSQL"
	@echo "  make db-restart - Перезапустить PostgreSQL"
	@echo "  make db-logs    - Показать логи"
	@echo "  make db-shell   - Подключиться к БД (psql)"
	@echo "  make db-import  - Импортировать init.sql"

db-up:
	@docker compose -f $(DOCKER_FILE) up -d
	@echo "Ожидание готовности БД (5 сек)..."
	@sleep 5

db-down:
	@docker compose -f $(DOCKER_FILE) down

db-restart: db-down db-up

db-logs:
	@docker compose -f $(DOCKER_FILE) logs -f

db-shell:
	@docker exec -it $(CONTAINER_NAME) psql -U $(DB_USER) -d $(DB_NAME)

db-import:
	@echo "Импорт $(INIT_SQL)..."
	@docker exec -i $(CONTAINER_NAME) psql -U $(DB_USER) -d $(DB_NAME) < $(INIT_SQL)
	@echo "Готово!"
