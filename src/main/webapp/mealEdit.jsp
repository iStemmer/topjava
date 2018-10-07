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
    ID : <input type="text" readonly="readonly" name="id"
                value="<c:out value="${meal.id}" />"/> <br/>
    Date time : <input type="text" name="dateTime"
                       value="<c:out value="${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy')}" />"/> <br/>
    Description : <input type="text" name="description"
                         value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input type="text" name="calories"
                      value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit" />

</form>

</body>
</html>
