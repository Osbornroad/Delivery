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


        ajaxUrl = "orderCheck/ajax/";
        var ajaxProperties = "orderCheck/properties/";

        $(document).ready(function() {
            $('#baseUpdated').hide();
            $('#baseUpdatedLabel').hide();

            table = $('#orderTable').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'copy', 'csv', 'excel', 'pdf', 'print'
                ],
                columns: [
                    {
                        data: "partNumber"
                    },
                    {
                        data: "necessaryAmount"
                    },
                    {
                        data: "ordered"
                    },
                    {
                        data: "diff"
                    }
                ]
            });

            $('#sendFilter').modal('show');

        });

        function enterPress() {
            renderTable();
        }

        function showErrorModal() {
            $('#waitingModal').modal('hide');
            $('#errorModal').modal('show');
            }

        function updateTableByData(data) {
            table.clear().rows.add(data).draw();
            $('#waitingModal').modal('hide');
        }

        function renderTable() {

            $('#filePath').val( $('#filePath').val().replaceAll('\"', ''));

            $('#sendFilter').modal('hide');
            $('#waitingModal').modal('show');

            $.ajax({
                type: "POST",
                url: ajaxUrl,
                data: $('#filter').serialize(),
                success: updateTableByData,
                error: showErrorModal
            });

            $('#waitingModal').modal('hide');

        }

    </script>
    <script src="//cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
    <script src="//cdn.datatables.net/buttons/2.0.1/js/dataTables.buttons.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>

    <script src="//cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
    <script src="//cdn.datatables.net/buttons/2.0.1/js/buttons.html5.min.js"></script>
    <script src="//cdn.datatables.net/buttons/2.0.1/js/buttons.print.min.js"></script>

    <link rel="stylesheet" href="//cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="//cdn.datatables.net/buttons/2.0.1/css/buttons.dataTables.min.css">


<%--    https://cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css--%>
<%--    https://cdn.datatables.net/buttons/2.0.1/css/buttons.dataTables.min.css--%>

<%--    https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js--%>
<%--    https://cdn.datatables.net/buttons/2.0.1/js/dataTables.buttons.min.js--%>

<%--    https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js--%>
<%--    https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js--%>

<%--    https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js--%>
<%--    https://cdn.datatables.net/buttons/2.0.1/js/buttons.html5.min.js--%>
<%--    https://cdn.datatables.net/buttons/2.0.1/js/buttons.print.min.js--%>

</head>

<body>
<div class="container float-left m-4">
    <div class = "row" style="height: 50px">
        <jsp:include page="fragments/navbar.jsp"/>

    </div>
    <div class="row mb-2">
        <div class="col-5 pl-0">
            <h4 id="title">Order analyse</h4>
        </div>
        <%--<div class="col-5">
            <a class="btn btn-outline-info float-right" onclick="tableToExcel('showData', 'EDI')">Export to Excel</a>
        </div>--%>
    </div>


    <div class="row">
        <table class="table table-hover table-striped display table-sm small" id="orderTable">
            <thead class="thead-light">
            <tr>
                <th class="p-1 align-middle text-left" style="width: 150px">Part Number</th>
                <th class="p-1 align-middle text-left" style="width: 100px">Necessary</th>
                <th class="p-1 align-middle text-left" style="width: 100px">Ordered</th>
                <th class="p-1 align-middle text-left" style="width: 100px">Difference</th>
            </tr>
            </thead>
        </table>
    </div>

    <%--<div class="row mb-0">
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
    </div>--%>
<%--    <div class="row mb-4">
        <div class="col-12">
            <p id='showData'></p>

        </div>
    </div>--%>
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
                <h2 class="modal-title" id="modalTitle">Paste path to order file</h2>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <sf:form class="form-horizontal" id="filter">

                    <div class="form-group">

                        <label for="filePath">Path to file</label>
                        <input type="text" class="form-control" id="filePath" name="filePath">
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
                        <button class="btn btn-primary float-right enter-pressed" id="submit" onclick="renderTable()" type="button">
                            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                        </button>
                    </div>
                </sf:form>

            </div>
        </div>
    </div>
</div>
