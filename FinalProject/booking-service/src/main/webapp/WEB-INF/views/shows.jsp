<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Мероприятия</title>
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
    <h1>Мероприятия</h1>

    <c:choose>
        <c:when test="${empty shows}">
            <p>Пока нет ни одного мероприятия.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Название</th>
                    <th>Начало</th>
                    <th>Площадка</th>
                    <th></th>
                </tr>
                <c:forEach items="${shows}" var="s">
                    <tr>
                        <td><c:out value="${s.title}"/></td>
                        <td>${s.startsAt}</td>
                        <td><c:out value="${s.venue}"/></td>
                        <td><a href="/shows/${s.id}">выбрать места</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <c:if test="${isAdmin}">
        <h2>Новое мероприятие</h2>
        <form method="post" action="/shows" class="grid">
            <input name="title" placeholder="Название" required/>
            <input type="datetime-local" name="startsAt" required/>
            <input name="venue" placeholder="Площадка"/>
            <label>Рядов <input type="number" name="rows" value="5" min="1" max="26"/></label>
            <label>Мест в ряду <input type="number" name="seatsPerRow" value="10" min="1" max="100"/></label>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">Создать</button>
        </form>
    </c:if>
</main>
</body>
</html>
