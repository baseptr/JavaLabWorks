COMPOSE = docker compose -f docker-compose.yml
DB_CTR  = postgres-task4
ROOT    = $(shell cd .. && pwd)

.PHONY: db run up down logs logs-db psql clean help

help:
	@echo "Запускать из директории Task_4_Docker/"
	@echo ""
	@echo "  make db       — запустить только БД (для локального запуска приложения)"
	@echo "  make run      — запустить приложение локально через Gradle (нужна запущенная БД)"
	@echo "  make up       — собрать и запустить всё через Docker Compose"
	@echo "  make down     — остановить все сервисы"
	@echo "  make logs     — логи приложения (compose)"
	@echo "  make logs-db  — логи базы данных"
	@echo "  make psql     — подключиться к БД через psql"
	@echo "  make clean    — остановить всё и удалить volumes"

db:
	$(COMPOSE) up db -d

run:
	cd $(ROOT) && ./gradlew :Task_4_Docker:bootRun

up:
	$(COMPOSE) up --build -d

down:
	$(COMPOSE) down

logs:
	$(COMPOSE) logs -f app

logs-db:
	$(COMPOSE) logs -f db

psql:
	docker exec -it $(DB_CTR) psql -U task4 -d task4

clean:
	$(COMPOSE) down -v
