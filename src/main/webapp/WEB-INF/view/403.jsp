<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>
    <jsp:include page="fragments/headerTags.jsp"/>
    <style>
        .alert {
            width:100%;
        }
    </style>
</head>
<body>


<div class="container">
    <div class = "row mb-5" style="height: 50px">
        <jsp:include page="fragments/navbar.jsp"/>

    </div>


    <div class="row mt-5 ml-0">
<%--        <div class="jumbotron">
            <strong>Attention!</strong> Access is denied.
        </div>--%>
        <div class="alert alert-danger d-inline-block text-center">
            <strong>Attention!</strong> Access is denied.
        </div>
<%--        <h3 class="text-center">Access is denied</h3>--%>
    </div>
</div>
<%--<h1>HTTP Status 403 - Access is denied</h1>

<c:choose>
    <c:when test="${empty username}">
        <h2>You do not have permission to access this page!</h2>
    </c:when>
    <c:otherwise>
        <h2>Username : ${username} <br/>
            You do not have permission to access this page!</h2>
    </c:otherwise>
</c:choose>--%>

</body>
</html>