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
        .kitRow {
            background: #ffe8a1;
        }
    </style>

    <script>

        ajaxUrl = "report/ajax/";

        $(document).ready(function() {
            var date = new Date();
            var currentDate = date.toISOString().slice(0,10);
            document.getElementById("endDate").value = currentDate;
            $('#sendFilter').modal('show');
        });


        function updateTable() {
            var title = $('input[name="typeReport"]:checked').val();
            $("#title").html(title === 'dailyReport' ? "Daily report" : "Monthy report");


            $('#sendFilter').modal('hide');
            $('#waitingModal').modal('show');
            $.ajax({
                type: "POST",
                url: ajaxUrl,
                data: $('#filter').serialize(),
                success: function (data, textStatus) { // вешаем свой обработчик на функцию success

                    // EXTRACT VALUE FOR HTML HEADER.
                    var col = data[0];

                    // CREATE DYNAMIC TABLE.
                    var table = document.createElement("table");
                    table.className += " table table-striped table-sm";


                    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

                    var tr = table.insertRow(-1);                   // TABLE ROW.

                    for (var i = 0; i < col.length; i++) {
                        var th = document.createElement("th");      // TABLE HEADER.
                        var integerDate;
                        if(col[i].length == 8) {
                            integerDate = col[i].substring(0, 4) + "-" + col[i].substring(4, 6) + "-" + col[i].substring(6);
                        } else {
                            integerDate = col[i].substring(0, 4) + "." + col[i].substring(4);
                        }
                        if (i == 0 || i == col.length - 1) {
                            th.innerHTML = col[i];
                        } else {
                            th.innerHTML = integerDate;
                        }
                        tr.appendChild(th);
                    }

                    // ADD JSON DATA TO THE TABLE AS ROWS.
                    for (var i = 1; i < data.length; i++) {
                        var row = data[i];
                        tr = table.insertRow(-1);
                        // if(i % 2 == 0)
                        //     tr.className += " "
                        for (var j = 0; j < row.length; j++) {
                            var td = document.createElement("td");
                            td.innerHTML = row[j];
                            tr.appendChild(td);
                            // var tabCell = tr.insertCell(-1);
                            if(j == 0)
                                td.className += " cellClass";
                            if(i == data.length - 1)
                                td.className += " totalRow";
                            if(i == data.length - 2)
                                td.className += " kitRow";
                            // tabCell.innerHTML = data[i][col[j]];
                        }

                    }

                    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
                    var divContainer = document.getElementById("showData");
                    divContainer.innerHTML = "";
                    divContainer.appendChild(table);
                    $('#waitingModal').modal('hide');

                },
                error: showErrorModal
            });

        }

        function showErrorModal() {
            $('#waitingModal').modal('hide');
            $('#errorModal').modal('show');
        }

        /*$(document).ready(function () {
            $.ajax({
                url: ajaxUrl,             // указываем URL и
                dataType: "json",                     // тип загружаемых данных
                success: function (data, textStatus) { // вешаем свой обработчик на функцию success

                    // EXTRACT VALUE FOR HTML HEADER.
                    var col = data[0];

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
                    for (var i = 1; i < data.length; i++) {
                        var row = data[i];
                        tr = table.insertRow(-1);
                        for (var j = 0; j < row.length; j++) {
                            var td = document.createElement("td");
                            td.innerHTML = row[j];
                            tr.appendChild(td);
                            // var tabCell = tr.insertCell(-1);
                            if(j == 0)
                                td.className += " cellClass";
                            if(i == data.length - 1)
                                td.className += " totalRow";
                            // tabCell.innerHTML = data[i][col[j]];
                        }

                    }

                    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
                    var divContainer = document.getElementById("showData");
                    divContainer.innerHTML = "";
                    divContainer.appendChild(table);

                }
            });
        })*/

        $(document).on('change', '.btn-file :file', function() {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [numFiles, label]);
        });

        var tableToExcel = (function() {
            var uri = 'data:application/vnd.ms-excel;base64,'
                , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
                , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
                , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
            return function(table, name) {
                if (!table.nodeType) table = document.getElementById(table)
                var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
                window.location.href = uri + base64(format(template, ctx))
            }
        })()

        function clickCheckbox() {
            var checkbox = document.getElementById("existProjectsCheckbox");
            var exist = document.getElementById("existProjects");
            var kitSelect = document.getElementById("kits");
            if (checkbox.checked) {
                kitSelect.disabled = true;
                exist.value = "true";
                kitSelect.value = "";
            } else {
                kitSelect.disabled = false;
                exist.value = "false";
            }
        }

    </script>


        </head>

<body>
<div class="container float-left mt-4">
    <div class = "row" style="height: 50px">
        <jsp:include page="fragments/navbar.jsp"/>

    </div>
    <div class="row mb-4">
        <div class="col-5">
            <h4 id="title">Report</h4>
        </div>
        <div class="col-5">
            <a class="btn btn-outline-info float-right" onclick="tableToExcel('showData', 'W3C Example Table')">Export to Excel</a>
        </div>
    </div>
    <div class="row mb-4">
        <div class="col-12">
            <p id='showData'></p>

        </div>
    </div>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="waitingModal" data-toggle="modal" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <p>Data are loading now. Please wait...</p>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="errorModal" data-toggle="modal" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Bad request</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Your request is wrong.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" role="dialog" id="sendFilter" tabindex = "-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title" id="modalTitle">Set filter</h2>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <sf:form class="form-horizontal" id="filter">
                    <div class="form-group" >
                        <div class="form-group" >
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="typeReport" id="dailyReport" value="dailyReport" checked>
                                <label class="form-check-label" for="dailyReport">Daily</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="typeReport" id="monthlyReport" value="monthlyReport">
                                <label class="form-check-label" for="monthlyReport">Monthly</label>
                            </div>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="aPoint" id="A001" value="A001">
                            <label class="form-check-label" for="A001">A001</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="aPoint" id="A080" value="A080">
                            <label class="form-check-label" for="A080">A080</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="aPoint" id="A090" value="A090" checked>
                            <label class="form-check-label" for="A090">A090</label>
                        </div>
                    </div>

                    <div class="form-group" >
                        <div class="row">
                            <div class="col">
                                <label class="control-label col-sm-3" for="startDate">Start</label>
                                <div>
                                    <input class="form-control" type="date" name="startDate" id="startDate">
                                </div>
                            </div>
                            <div class="col">
                                <label class="control-label col-sm-3" for="endDate">End</label>
                                <div>
                                    <input class="form-control" type="date"  name="endDate" id="endDate">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" id="existProjectsCheckbox" checked onclick="clickCheckbox()">
                            <label class="form-check-label" for="existProjectsCheckbox">Exist projects</label>
                        </div>
                    </div>
                    <input type="hidden" id="existProjects" name="existProjects" value="true">
                    <div class="form-group">
                        <label for="kits" class="control-label col-xs-3">Select finish parts holding "Ctrl"</label>
                        <div class="col-xs-9">
                            <select class="finishPartsSelect form-control to-empty not-empty" id="kits" multiple name="kits" disabled>
                                <c:forEach var="item" items="${kits}">
                                    <option value="${item}">${item}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <button class="btn btn-primary float-right" onclick="updateTable()" type="button">
                            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                        </button>
                    </div>
                </sf:form>
                <%--<div class="col">
                    <a class="btn btn-danger" type="button" onclick="clearFilter()">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    </a>
                    <a class="btn btn-primary" type="button" onclick="updateTable()">
                        <span class="glyphicon glyphicon-filter" aria-hidden="true"></span>
                    </a>
                </div>--%>
            </div>
        </div>
    </div>
</div>
</body>