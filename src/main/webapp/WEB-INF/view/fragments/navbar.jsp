<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
    /* unvisited link */
    a:link {
        color: lightskyblue;
    }

    /* mouse over link */
    a:hover {
        color: white;
    }

    /* selected link */
    a:active {
        color: white;
    }
</style>

<script>

    navbarUrl = "navbar";

    $(document).ready(function() {
        updateDate();
    });

    function updateDate() {
        $.ajax({
            type: "GET",
            url: navbarUrl,
            success: function (data) {
                var options = { year: 'numeric', month: 'short', day: 'numeric', hour:  'numeric', minute:  'numeric'};
                var month = data[1] - 1;
                var date = new Date(data[0], month, data[2], data[3], data[4]/*, data[5], data[6]*/);
                var strDate = date.toLocaleDateString("en-UK", options);
                $('#baseUpdated').html(strDate);
            }
        });
    }

</script>

<nav class="navbar fixed-top navbar-expand-sm bg-primary navbar-dark">
    <a class="navbar-brand" href="<c:url value="/"/>">SANOH</a>

        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="reports" data-toggle="dropdown">Reports</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="<c:url value="/forecast"/>">BBSS</a>
                        <a class="dropdown-item" href="<c:url value="/report"/>">Report</a>
                        <a class="dropdown-item" href="<c:url value="/notes"/>">Database</a>
                        <a class="dropdown-item" href="<c:url value="/edi"/>">EDI</a>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="catalogs" data-toggle="dropdown">Catalogs</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="<c:url value="/finishParts"/>">Finish parts</a>
                        <a class="dropdown-item" href="<c:url value="/kits"/>">Kits</a>
                        <a class="dropdown-item" href="<c:url value="/parts"/>">Parts</a>
                    </div>
                </li>
<%--                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="loader" data-toggle="dropdown">Loader</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="<c:url value="/parts/load"/>">Parts loader</a>
                    </div>
                </li>--%>
            </ul >
            <span class="navbar-text pl-5" id="baseUpdatedLabel">
                    Base updated:
            </span>
            <span class="navbar-text pl-1" id="baseUpdated"></span>
            <div class="navbar-nav ml-auto">
                        <c:choose>
                            <c:when test="${pageContext.request.userPrincipal.name == null}">
                                <a href="<c:url value="/login" />" class="nav-item nav-link">Login</a>
                            </c:when>
                            <c:otherwise>
                                <sec:authorize access="isAuthenticated()">
                                    <span class="navbar-text text-dark mr-1 pl-0">${pageContext.request.userPrincipal.name}</span>
                                    <span class="navbar-text text-dark">|</span>
                                    <form:form action="${pageContext.request.contextPath}/logout" class="mb-0" method="POST">
                                        <input type="submit" class="btn btn-link nav-item nav-link pl-1" value="Logout" />
                                    </form:form>
                                </sec:authorize>
                            </c:otherwise>
                        </c:choose>
            </div>
        </div>

</nav>