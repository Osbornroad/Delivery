<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>--%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
    /* unvisited link */
    a:link {
        color: black;
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
            success: function (data, textStatus) {
                var options = { year: 'numeric', month: 'short', day: 'numeric', hour:  'numeric', minute:  'numeric'};
                var month = data[1] - 1;
                var date = new Date(data[0], month, data[2], data[3], data[4]/*, data[5], data[6]*/);
                var strDate = date.toLocaleDateString("en-UK", options);
                $('#baseUpdated').html(strDate);
            }
        });
    }

    setInterval(updateDate, 60000);

</script>

<nav class="navbar fixed-top navbar-expand-sm bg-primary navbar-dark">
    <a class="navbar-brand" href="<c:url value="/"/>">SANOH</a>

            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#myNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav">

                <%--<li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="initLoad" data-toggle="dropdown">
                        Init Load
                    </a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item disabled" href="<c:url value="/wib224"/>">Load Wib224</a>
                        <a class="dropdown-item disabled"  href="<c:url value="/nissan"/>">Load Nissan</a>
                        <a class="dropdown-item disabled" href="<c:url value="/firebird"/>">Load Firebird</a>
                        <a class="dropdown-item disabled" href="<c:url value="/finishPartsLoading"/>">Load Finish Parts</a>
                        <a class="dropdown-item disabled" href="<c:url value="/walkFTP"/>">Check FTP</a>
                    </div>
                </li>--%>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="reports" data-toggle="dropdown">Reports</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="<c:url value="/forecast"/>">Forecast</a>
                        <a class="dropdown-item" href="<c:url value="/report"/>">Report</a>
                        <a class="dropdown-item" href="<c:url value="/notes"/>">Database</a>
<%--                        <a class="dropdown-item" href="<c:url value="/monthlyReport"/>">Monthly Report</a>--%>
                        <a class="dropdown-item" href="<c:url value="/edi"/>">EDI</a>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="catalogs" data-toggle="dropdown">Catalogs</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="<c:url value="/finishParts"/>">Finish parts</a>
                        <a class="dropdown-item" href="<c:url value="/kits"/>">Kits</a>
                    </div>
<%--                </li>
                li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="setting" data-toggle="dropdown">Setting</a>
                <div class="dropdown-menu">
                    <a class="dropdown-item disabled" href="<c:url value="/setting"/>">Setting</a>
                </div>
                </li>--%>

            </ul >
            <span class="navbar-text pl-5" id="baseUpdatedLabel">
                    Base updated:
            </span>
            <span class="navbar-text pl-1" id="baseUpdated"></span>

            <ul class="nav navbar-nav ml-auto">
                <li>
                    <form:form action="logout" method="post">
                        <sec:authorize access="isAuthenticated()">
                            <c:if test="${pageContext.request.userPrincipal.name != null}">
                                <span class="glyphicon glyphicon-user"></span>
                                    ${pageContext.request.userPrincipal.name}  |  <button class="btn btn-primary" type="submit">
                                        <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
                                    </button>
                            </c:if>
                        </sec:authorize>
                    </form:form>


                <%--                    <sec:authorize access="isAuthenticated()">
                        &lt;%&ndash;authenticated as <sec:authentication property="principal.username" />&ndash;%&gt;
                        <!-- For login user -->
                        <c:url value="/j_spring_security_logout" var="logoutUrl" />
                        <form action="${logoutUrl}" method="post" id="logoutForm">
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}" />
                        </form>
                        <script>
                            function formSubmit() {
                                document.getElementById("logoutForm").submit();
                            }
                        </script>

                        <c:if test="${pageContext.request.userPrincipal.name != null}">
                            <span class="glyphicon glyphicon-user"></span>
                            ${pageContext.request.userPrincipal.name}  |  <a href="javascript:formSubmit()">
                            <span class="glyphicon glyphicon-log-out"></span> Logout</a>
                        </c:if>
                    </sec:authorize>
                </li>--%>
            </ul>
        </div>

</nav>