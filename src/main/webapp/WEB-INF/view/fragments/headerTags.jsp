<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%--Csfr token--%>
<%--    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>--%>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/datatables/1.10.21/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap4-glyphicons/css/bootstrap-glyphicons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/datetimepicker/2.5.20/jquery.datetimepicker.css">
    <script src="${pageContext.request.contextPath}/webjars/jquery/3.5.1/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/webjars/jquery/3.5.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/webjars/datatables/1.10.21/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/webjars/datatables/1.10.21/js/dataTables.bootstrap4.min.js"></script>
    <script src="${pageContext.request.contextPath}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootbox.min.js"></script>
<%--    <script src="${pageContext.request.contextPath}/resources/js/moment.js"></script>--%>
<%--    <script src="${pageContext.request.contextPath}/resources/js/generalScripts.js"></script>--%>



</head>