<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://javawebinar.ru/functions" prefix="f" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
    <p><a href="meals?action=add">Add User</a></p>
    <table border="0" cellpadding="5" cellspacing="5" align="center">
        <tr>
            <th>Порядковый номер</th><th>Дата</th><th>Описание</th><th>Калории</th><th colspan="2">Действия</th>
        </tr>
        <c:forEach var="meal" items="${listMeal}" varStatus="varStatus">
            <tr ${varStatus.index mod 2 > 0 ? 'bgcolor="#d3d3d3"' : ''} style="color: ${meal.exceed ? 'red' : 'green'}">
                <td>${varStatus.index+1}</td>
                <td>${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy')}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Изменить</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Удалить</a></td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>