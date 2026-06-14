<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Мои брони</title>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>
<nav>
    <a href="/shows">Мероприятия</a>
    <a href="/my-bookings">Мои брони</a>
    <form method="post" action="/logout" class="inline">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="link">Выйти</button>
    </form>
</nav>
<main class="card">
    <h1>Мои брони</h1>

    <c:if test="${message != null}">
        <p class="flash ok">${message}</p>
    </c:if>
    <c:if test="${error != null}">
        <p class="flash error">${error}</p>
    </c:if>

    <c:choose>
        <c:when test="${empty bookings}">
            <p>Броней пока нет — <a href="/shows">выбрать мероприятие</a>.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>№</th>
                    <th>Мероприятие</th>
                    <th>Место</th>
                    <th>Статус</th>
                    <th>Холд до</th>
                    <th>Создана</th>
                </tr>
                <c:forEach items="${bookings}" var="b">
                    <tr>
                        <td>${b.id()}</td>
                        <td><c:out value="${b.showTitle()}"/></td>
                        <td>${b.seatLabel()}</td>
                        <td><span class="status ${b.status()}">${b.status()}</span></td>
                        <td>${b.heldUntil()}</td>
                        <td>${b.createdAt()}</td>
                    </tr>
                </c:forEach>
            </table>
            <p class="hint">Статус обновляется сагой: HELD → CONFIRMED / CANCELLED / EXPIRED. Обновите страницу.</p>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
