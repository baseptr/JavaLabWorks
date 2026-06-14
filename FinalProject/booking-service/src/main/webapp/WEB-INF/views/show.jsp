<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title><c:out value="${show.title}"/></title>
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
    <h1><c:out value="${show.title}"/></h1>
    <p>${show.startsAt} — <c:out value="${show.venue}"/></p>

    <p class="legend">
        <span class="seat free">1</span> свободно
        <span class="seat taken">1</span> занято
    </p>

    <div class="seat-map">
        <c:forEach items="${rows}" var="row">
            <div class="seat-row">
                <span class="row-label">${row.key}</span>
                <c:forEach items="${row.value}" var="seat">
                    <c:choose>
                        <c:when test="${seat.free()}">
                            <form method="post" action="/shows/${show.id}/book" class="inline">
                                <input type="hidden" name="seatId" value="${seat.id()}"/>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="seat free" title="Ряд ${row.key}, место ${seat.seatNumber()}">
                                        ${seat.seatNumber()}
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <span class="seat taken">${seat.seatNumber()}</span>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:forEach>
    </div>
</main>
</body>
</html>
