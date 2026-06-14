<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Вход</title>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>
<main class="card narrow">
    <h1>Бронирование билетов</h1>

    <c:if test="${param.error != null}">
        <p class="flash error">Неверный логин или пароль</p>
    </c:if>
    <c:if test="${param.logout != null}">
        <p class="flash ok">Вы вышли из системы</p>
    </c:if>

    <form method="post" action="/login">
        <label>Логин
            <input name="username" required autofocus/>
        </label>
        <label>Пароль
            <input type="password" name="password" required/>
        </label>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit">Войти</button>
    </form>
</main>
</body>
</html>
