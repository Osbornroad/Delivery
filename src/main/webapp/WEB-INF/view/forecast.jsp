<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>

    <jsp:include page="fragments/headerTags.jsp"/>

    <style>
        th, td, p, input, h3 {
            font:14px 'Segoe UI';
        }
        table, th, td {
            border: solid 1px #ddd;
            border-collapse: collapse;
            padding: 2px 3px;
            text-align: center;
        }
        th {
            font-weight:bold;
            background: #adb5bd;
            /*width: 10px;*/
        }
        td {
            width: 60px;
        }
        .cellClass {
            width: 100px;
        }
        .totalRow {
            font-weight: 700;
        }

    </style>
    <script>

        ajaxUrl = "forecast/ajax/";

        $(document).ready(function () {
            $.ajax({
                url: ajaxUrl,             // указываем URL и
                dataType: "json",                     // тип загружаемых данных
                success: function (data, textStatus) { // вешаем свой обработчик на функцию success

                        // EXTRACT VALUE FOR HTML HEADER.
                        var col = [];
                        for (var i = 0; i < data.length; i++) {
                            for (var key in data[i]) {
                                if (col.indexOf(key) === -1) {
                                    // formatDate()
                                    col.push(key);
                                }
                            }
                        }

                        // CREATE DYNAMIC TABLE.
                        var table = document.createElement("table");

                        // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

                        var tr = table.insertRow(-1);                   // TABLE ROW.

                        for (var i = 0; i < col.length; i++) {
                            var th = document.createElement("th");      // TABLE HEADER.
                            th.innerHTML = col[i];
                            tr.appendChild(th);
                        }

                        // ADD JSON DATA TO THE TABLE AS ROWS.
                        for (var i = 0; i < data.length; i++) {

                            tr = table.insertRow(-1);
    /*                        if(i == data.length - 1)
                                tr.className += "totalRow";*/

                            for (var j = 0; j < col.length; j++) {
                                var tabCell = tr.insertCell(-1);
                                if(j == 0)
                                    tabCell.className += " cellClass";
                                if(i == data.length - 1)
                                    tabCell.className += " totalRow";
                                tabCell.innerHTML = data[i][col[j]];
                            }
                        }

                        // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
                        var divContainer = document.getElementById("showData");
                        divContainer.innerHTML = "";
                        divContainer.appendChild(table);

                }
            });
        })

    </script>


</head>
<body>
<div class="container float-left mt-4">
    <div class = "row" style="height: 50px">
        <jsp:include page="fragments/navbar.jsp"/>

    </div>
    <div class="row mb-4">
        <div class="col-5">
            <h4>Forecast</h4>
        </div>
        <div class="col-5">
            <a class="btn btn-outline-info float-right" onclick="tableToExcel('showData', 'Forecast')">Export to Excel</a>
        </div>
    </div>
    <div class="row mb-4">
        <div class="col-12">
            <p id='showData'></p>
        </div>
    </div>
</div>
</body>