<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://javawebinar.ru/functions" prefix="f" %>
<html>
<head>
    <meta charset="utf-8" />
    <title>Meal</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddUser">
    <table>
        <tr>
            <th>Id</th>
            <th>Дата и время</th>
            <!--<th></th>-->
            <th>Описание</th>
            <th>Калории</th>
        </tr>
        <tr>
            <td><input type="text" readonly="readonly" name="id" value="<c:out value="${meal.id}" />"/></td>
            <td><input type="datetime-local"  name="dateTime" value="<c:out value="${meal.dateTime}"/>"/></td>
            <td><input type="text" name="description" value="<c:out value="${meal.description}" />"/></td>
            <td><input type="text" name="calories" value="<c:out value="${meal.calories}" />"/></td>
        </tr>
    </table>
    <input type="submit" value="Submit" />
</form>

</body>
</html>
