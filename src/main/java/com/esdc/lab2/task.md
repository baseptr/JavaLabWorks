# Task DB: Coffee Stats — консольное приложение на чистом JDBC

## Описание проекта

Проект `coffee-stats` — консольное приложение кофейни без Spring.
Пользователь запускает программу в терминале, делает заказы и смотрит статистику.
Слоя веб-сервера нет — только Java + сырой JDBC + PostgreSQL.

### Структура проекта

```
coffee-stats/
├── docker-file.yaml             ← PostgreSQL в Docker
├── init.sql                     ← схема БД и начальные данные
├── pom.xml                      ← Maven-проект (Java 25, пока только Lombok)
└── src/main/java/org/example/
    ├── Main.java                ← REPL-цикл, точка входа (реализован)
    ├── dto/
    │   └── CoffeeStats.java     ← DTO для статистики
    ├── entity/
    │   ├── Coffee.java          ← сущность напитка
    │   ├── Order.java           ← сущность заказа
    │   └── User.java            ← сущность пользователя
    ├── repository/
    │   ├── DataSourceProvider.java   ← !! нужно реализовать
    │   ├── CoffeeRepository.java     ← !! нужно реализовать
    │   ├── OrderRepository.java      ← !! нужно реализовать
    │   └── UserRepository.java       ← !! нужно реализовать
    └── service/
        ├── CoffeService.java         ← готов (делегирует в репозиторий)
        ├── OrderService.java         ← почти готов, один метод нужно реализовать
        ├── UserService.java          ← почти готов, один опциональный метод
        ├── OrderProcessor.java       ← готов (логика оформления заказа)
        └── StatsProcessor.java       ← готов (вычисляет статистику через сервисы)
```

### Схема базы данных

```
users                coffee
─────────────        ─────────────────
id   BIGSERIAL PK    id    BIGSERIAL PK
name VARCHAR         name  VARCHAR
                     price DECIMAL

orders                         order_items (связь M:N)
────────────────────────        ──────────────────────
id          BIGSERIAL PK        order_id  FK → orders.id   (ON DELETE CASCADE)
user_id     FK → users.id       coffee_id FK → coffee.id
total_price DECIMAL             PK (order_id, coffee_id)
created_at  TIMESTAMP
```

В `init.sql` уже добавлены 10 напитков (Espresso, Americano, Cappuccino, Latte и др.).

### Как запустить PostgreSQL

```bash
cd semeser5/lessons/lesson0.3/task/coffee-stats
docker compose -f docker-file.yaml up -d
```

База: `coffeedb`, пользователь: `coffeeuser`, пароль: `coffeepass`, порт: `5437`.


## Основное задание

### Шаг 1 — Добавьте зависимость в `pom.xml`

В файле `pom.xml` сейчас есть только Lombok. Добавьте:

- **PostgreSQL JDBC Driver** — `org.postgresql:postgresql` — чтобы Java умела подключаться к PostgreSQL.

### Шаг 2 — Реализуйте `DataSourceProvider.getDataSource()`

Файл: `repository/DataSourceProvider.java`

Метод `getDataSource()` сейчас пустой и ничего не возвращает.

Нужно:
1. Прочитать файл `src/main/resources/db.properties` (там уже есть url, username, password).
2. Создать `DriverManager`-соединение или простой `DataSource` на основе этих параметров и вернуть его.

### Шаг 3 — Реализуйте `CoffeeRepository`

Файл: `repository/CoffeeRepository.java`

| Метод | Что делает | SQL-подсказка |
|---|---|---|
| `getAllCoffees()` | Возвращает все напитки из таблицы `coffee` | `SELECT id, name, price FROM coffee` |
| `getCoffeById(long id)` | Возвращает один напиток по id | `SELECT ... WHERE id = ?` |
| `getMostPopularCoffee()` | Напиток, который заказывали чаще всего | JOIN с `order_items`, GROUP BY, ORDER BY COUNT DESC LIMIT 1 |
| `getLeastPopularCoffee()` | Напиток, который заказывали реже всего | то же, но ORDER BY COUNT ASC |

Для каждого метода: получите соединение из `DataSourceProvider`, выполните запрос, смапьте `ResultSet` в объект/список `Coffee`.

### Шаг 4 — Реализуйте `UserRepository`

Файл: `repository/UserRepository.java`

| Метод | Что делает | SQL-подсказка |
|---|---|---|
| `getTotalUsers()` | Количество пользователей в таблице | `SELECT COUNT(*) FROM users` |
| `getAllUsers()` | Список всех пользователей | `SELECT id, name FROM users` |
| `getUser(long id)` | Пользователь по id | `WHERE id = ?` |
| `getUserByUserName(String name)` | Пользователь по имени | `WHERE name = ?` |
| `save(User user)` | Сохранить нового пользователя и вернуть его с присвоенным id | `INSERT INTO users(name) VALUES (?) RETURNING id` |
| `getUsersByOrderedCoffee(Long coffeeId)` | Пользователи, которые заказывали конкретный напиток | JOIN: `users → orders → order_items`, `WHERE order_items.coffee_id = ?` |

### Шаг 5 — Реализуйте `OrderRepository`

Файл: `repository/OrderRepository.java`

> **Обратите внимание:** метод `getAllOrders()` объявлен с неправильным возвращаемым типом `List<OrderRepository>` — это нужно исправить на `List<Order>`.

| Метод | Что делает | SQL-подсказка |
|---|---|---|
| `getCount()` | Общее количество заказов | `SELECT COUNT(*) FROM orders` |
| `getAllOrders()` | Список всех заказов | SELECT из `orders`; для каждого заказа также достать список напитков через `order_items` |
| `getOrder(long id)` | Один заказ по id | `WHERE id = ?` |
| `getOrdersByCoffeeId(Long coffeeId)` | Все заказы, в которых есть конкретный напиток | JOIN `orders → order_items WHERE coffee_id = ?` |
| `save(Order order)` | Сохранить заказ | 1) `INSERT INTO orders(user_id, total_price, created_at)` → получить сгенерированный id; 2) для каждого `Coffee` в `order.getItems()` сделать `INSERT INTO order_items(order_id, coffee_id)` |

> **Совет для `save`:** используйте `PreparedStatement` с флагом `Statement.RETURN_GENERATED_KEYS`, чтобы получить id созданного заказа обратно.

### Шаг 6 — Реализуйте `OrderService.getTotalRevenue()`

Файл: `service/OrderService.java`

Метод сейчас возвращает `null`. Реализуйте подсчёт суммы `total_price` по всем заказам.
Можно либо добавить отдельный SQL-запрос `SELECT SUM(total_price) FROM orders` в репозиторий, либо посчитать на Java-стороне через уже реализованный `getAllOrders()`.

---

## Дополнительное задание (Optional)

### 1. Напиток с максимальной выручкой (`getMostRevenueGeneratingCoffee`)

Реализуйте метод в `CoffeeRepository`. Нужен SQL с JOIN на `order_items` и суммированием цены:

```sql
SELECT c.id, c.name, c.price, SUM(c.price) AS revenue
FROM coffee c
JOIN order_items oi ON c.id = oi.coffee_id
GROUP BY c.id, c.name, c.price
ORDER BY revenue DESC
LIMIT 1
```

### 2. Своя метрика с JOIN на 2+ таблицы

Придумайте и реализуйте собственную метрику, для вычисления которой нужен JOIN минимум двух таблиц.

Примеры идей:
- Средний чек по пользователям (`AVG(total_price) GROUP BY user_id`)
- Топ-3 пользователей по суммарным тратам
- Самый «дорогой» заказ с именем пользователя

Добавьте метод в нужный репозиторий, вызовите его через сервис и зарегистрируйте как новый `StatCalculator` в `StatsProcessor`.

### 3. Пул соединений HikariCP

Замените простой `DataSource` в `DataSourceProvider` на пул соединений HikariCP.

1. Добавьте зависимость `com.zaxxer:HikariCP` в `pom.xml`.
2. Создайте `HikariDataSource` вместо обычного соединения:

### 4. Кэш

Добавьте кэширование результатов для любого часто вызываемого метода репозитория (например, `getAllCoffees()` — список меню не меняется).

Простейший вариант — поле `private List<Coffee> cache` в репозитории, которое заполняется при первом обращении и возвращается при последующих без похода в БД.

---


## Чек-лист для проверки

- [ ] PostgreSQL поднимается через `docker compose` без ошибок
- [ ] Зависимость PostgreSQL JDBC добавлена в `pom.xml`
- [ ] `DataSourceProvider.getDataSource()` возвращает рабочий `DataSource`
- [ ] `menu` выводит список напитков
- [ ] `order 1 2 Мария` создаёт заказ и сохраняет его в БД
- [ ] Повторный запуск `order ... Мария` переиспользует существующего пользователя (не создаёт дубликат)
- [ ] `stats 1` выводит самый популярный напиток
- [ ] `stats 2` выводит самый непопулярный напиток
- [ ] `stats 6` выводит общее число заказов
- [ ] `stats 7` выводит суммарную выручку