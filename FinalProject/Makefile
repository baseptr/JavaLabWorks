COMPOSE = docker compose
GRADLE  = cd .. && ./gradlew

.DEFAULT_GOAL := help

.PHONY: help infra up down ps logs build booking payment notification audit clean reset

help:           ## список целей
	@grep -E '^[a-z-]+:.*##' $(MAKEFILE_LIST) | awk -F ':.*## ' '{printf "  %-14s %s\n", $$1, $$2}'

infra:          ## поднять только postgres + rabbitmq
	$(COMPOSE) up -d postgres rabbitmq

up:             ## собрать и поднять всё в докере
	$(COMPOSE) up -d --build

down:           ## остановить и удалить контейнеры
	$(COMPOSE) down

ps:             ## статус контейнеров
	$(COMPOSE) ps

logs:           ## логи всех сервисов
	$(COMPOSE) logs -f --tail=100

build:          ## компиляция всех модулей
	$(GRADLE) compileJava

booking:        ## запустить booking-service локально (порт 8080)
	$(GRADLE) :FinalProject:booking-service:bootRun

payment:        ## запустить payment-service локально (порт 8083)
	$(GRADLE) :FinalProject:payment-service:bootRun

notification:   ## запустить notification-service локально (порт 8081)
	$(GRADLE) :FinalProject:notification-service:bootRun

audit:          ## запустить audit-service локально (порт 8082)
	$(GRADLE) :FinalProject:audit-service:bootRun

clean:          ## gradle clean
	$(GRADLE) clean

reset:          ## снести контейнеры вместе с данными (volumes)
	$(COMPOSE) down -v
