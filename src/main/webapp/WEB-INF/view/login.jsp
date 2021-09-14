<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@page session="true"%>
<html lang="en">
<head>

    <jsp:include page="fragments/headerTags.jsp"/>

</head>
<body onload='document.loginForm.username.focus();'>

<div class="container">

    <div class="row mt-5">
        <div class="col"></div>
        <div class="col">
            <form name='loginForm'
                  action="<c:url value='/j_spring_security_check' />" method='POST'>

                <div class="form-group">
                    <h3>Sign in</h3>

                    <c:if test="${not empty error}">
                        <div class="error">${error}</div>
                    </c:if>
                    <c:if test="${not empty msg}">
                        <div class="msg">${msg}</div>
                    </c:if>
                </div>


                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" placeholder="Enter Username" class="form-control" name="username" id="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" placeholder="Enter Password" class="form-control" name="password" id="password" required>
                </div>
                <div class="form-group">
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}" />
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block" >Login</button>
                </div>
                <div class="form-group">
                    <a href="<c:url value="/"/>" class="btn btn-secondary btn-block" role="button">Cancel</a>
                </div>
            </form>
        </div>
        <div class="col"></div>
    </div>
</div>

</body>
</html>