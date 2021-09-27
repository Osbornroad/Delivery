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


        ajaxUrl = "edi/ajax/";
        var ajaxProperties = "edi/properties/";

        $(document).ready(function() {
            $('#baseUpdated').hide();
            $('#baseUpdatedLabel').hide();
            $('#sendFilter').modal('show');

        });

        function enterPress() {
            renderTable();
        }

        function renderTable() {

            $('#filePath').val( $('#filePath').val().replaceAll('\"', ''));

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
                        th.innerHTML = col[i];
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

            $.ajax({
                type: "POST",
                url: ajaxProperties,
                data: $('#filter').serialize(),
                success: function (data, textStatus) {
                    $("#ediNumber").html(data[0]);
                    $("#ediDate").html(data[1]);
                    $("#title").html(data[2]);

                }
            });
        }

    </script>


</head>

<body>
<div class="container float-left mt-4">
    <div class = "row" style="height: 50px">
        <jsp:include page="fragments/navbar.jsp"/>

    </div>
    <div class="row mb-2">
        <div class="col-5">
            <h4 id="title">EDI forecast</h4>
        </div>
        <div class="col-5">
            <a class="btn btn-outline-info float-right" onclick="tableToExcel('showData', 'EDI')">Export to Excel</a>
        </div>
    </div>
    <div class="row mb-0">
        <div class="col-2">
            <p>EDI number:</p>
        </div>
        <div class="col-2">
            <p id="ediNumber"></p>
        </div>
    </div>
    <div class="row mb-2 mt-0">
        <div class="col-2">
            <p>EDI date:</p>
        </div>
        <div class="col-2">
            <p id="ediDate"></p>
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
                <h2 class="modal-title" id="modalTitle">Paste path to EDI csv file</h2>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <sf:form class="form-horizontal" id="filter">

                    <div class="form-group">
                        <input type="text" class="form-control" id="filePath" name="filePath">
                    </div>
                    <div class="form-group" >
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="ediDir" id="dailyEdiDir" value="dailyEdiDir" checked>
                            <label class="form-check-label" for="dailyEdiDir">Daily</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="ediDir" id="weeklyEdiDir" value="weeklyEdiDir">
                            <label class="form-check-label" for="weeklyEdiDir">Weekly</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary float-right enter-pressed" id="submit" onclick="renderTable()" type="button">
                            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                        </button>
                    </div>
                </sf:form>

            </div>
        </div>
    </div>
</div>
</body>