<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

    <head>

        <jsp:include page="fragments/headerTags.jsp"/>

        <script>

            ajaxUrl = "notes/ajax/";

            function enterPress() {
                updateTable();
            }

            function updateTable() {
                $('#sendFilter').modal('hide');
                $('#waitingModal').modal('show');
                $.ajax({
                    type: "POST",
                    url: ajaxUrl + "filter",
                    data: $('#filter').serialize(),
                    success: updateTableByData,
                    error: showErrorModal
                });
            }

            function updateTableByData(data) {
                table.clear().rows.add(data).draw();
                $('#waitingModal').modal('hide');
            }


            // End of general scripts

            function formatDate(date) {
                var options = {year: 'numeric', month: 'numeric', day: 'numeric', timezone: 'UTC', hour: 'numeric', minute: 'numeric'};
                var year = date[0];
                var month = date[1];
                var day = date[2];
                var hour = date[3];
                var min = date[4];
                var dateTime = new Date(year, month, day, hour, min);
                var renderDateTime = dateTime.toLocaleDateString("ru", options);
                return renderDateTime;
            }

            function addZero(data) {
                if (data.length == 1) {
                    data = '0' + data;
                }
                return data;
            }


/*            function showErrorModal() {
                $('#waitingModal').modal('hide');
                $('#errorModal').modal('show');
            }*/

            function clearFilter() {

            }


            $(document).ready(function () {
                table = $('#notesTable').DataTable({
                    ajax : {
                        url : ajaxUrl,
                        dataSrc : ""
                    },
                    columns : [
                        {
                            data: "id"
                        },
                        {
                            data: "fieldKey"
                        },
                        {
                            data: "apoint"
                        },
                        {
                            data: "sequence"
                        },
                        {
                            data: "modelVariant"
                        },
                        {
                            data: "series"
                        },
                        {
                            data: "number"
                        },
                        {
                            data: "planned",
                            sType : "date-uk",
                            render : function (date, type, row) {
                                if (type === 'display') {
                                    return formatDate(date);
                                }
                                return date;
                            }
                        },
                        {
                            data: "wib225"
                        },
                        {
                            data: "wib224"
                        },
                        {
                            data: "apointDateTime",
                            sType : "date-uk",
                            render : function (date, type, row) {
                                if (type === 'display') {
                                    return formatDate(date);
                                }
                                return date;
                            }
                        }
                    ]
                });
            });

            jQuery.extend( jQuery.fn.dataTableExt.oSort, {
                "date-uk-pre": function ( a ) {
                    var year = a.year.toString();
                    var month = addZero(a.monthValue.toString());
                    var day = addZero(a.dayOfMonth.toString());
                    var hour = addZero(a.hour.toString());
                    var min = addZero(a.minute.toString());
                    var dateString = year + month + day + hour + min;
                    return dateString;
                },
                "date-uk-asc": function ( a, b ) {
                    return ((a < b) ? -1 : ((a > b) ? 1 : 0));
                },
                "date-uk-desc": function ( a, b ) {
                    return ((a < b) ? 1 : ((a > b) ? -1 : 0));
                }
            } );

        </script>

    </head>

    <body>

        <div class="container float-left m-4">
            <div class = "row" style="height: 50px">
                <jsp:include page="fragments/navbar.jsp"/>

            </div>
            <div class="row mb-4">
                <div class="col-2 pl-0">
                    <h4>Database</h4>
                </div>
                <div class="col text-left">
                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#sendFilter">Filter</button>
                </div>
            </div>
            <div class="row">
                    <table class="table table-hover table-striped display table-sm small" id="notesTable">
                        <thead class="thead-light">
                        <tr>
<%--                            <th class="p-1 align-middle text-left">ID</th>
                            <th class="p-1 align-middle text-left">F-Key</th>
                            <th class="p-1 align-middle text-left">A-Point</th>
                            <th class="p-1 align-middle text-left">Sequence</th>
                            <th class="p-1 align-middle text-left">Model Variant</th>
                            <th class="p-1 align-middle text-left">Series</th>
                            <th class="p-1 align-middle text-left">Number</th>
                            <th class="p-1 align-middle text-left">Planned</th>
                            <th class="p-1 align-middle text-left">Wib 225</th>
                            <th class="p-1 align-middle text-left">Wib 224</th>
                            <th class="p-1 align-middle text-left">A-point Date</th>--%>

                            <th class="p-1 align-middle text-left" style="width: 60px">ID</th>
                            <th class="p-1 align-middle text-left" style="width: 60px">F-Key</th>
                            <th class="p-1 align-middle text-left" style="width: 55px">A-Point</th>
                            <th class="p-1 align-middle text-left" style="width: 75px">Sequence</th>
                            <th class="p-1 align-middle text-left" style="width: 150px">Model Variant</th>
                            <th class="p-1 align-middle text-left" style="width: 45px">Series</th>
                            <th class="p-1 align-middle text-left" style="width: 45px">Number</th>
                            <th class="p-1 align-middle text-left" style="width: 100px">Planned</th>
                            <th class="p-1 align-middle text-left" style="width: 60px">Wib 225</th>
                            <th class="p-1 align-middle text-left" style="width: 60px">Wib 224</th>
                            <th class="p-1 align-middle text-left" style="width: 120px">A-point Date</th>
                        </tr>
                        </thead>
                    </table>
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
                        <p>Requested data are too large. Use filter for reduce less than 10000 notes.</p>
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
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="A001" id="A001" value="A001">
                                    <label class="form-check-label" for="A001">A001</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="A080" id="A080" value="A080">
                                    <label class="form-check-label" for="A080">A080</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="A090" id="A090" value="A090" checked>
                                    <label class="form-check-label" for="A090">A090</label>
                                </div>
                            </div>

                            <div class="form-group" >

                                <label class="control-label col-sm-3" for="startDate">Start</label>

                                <div class="col-sm-3">
                                    <input class="form-control" type="date" style="width:300px" name="startDate" id="startDate">
                                </div>

                                <label class="control-label col-sm-3" for="endDate">End</label>

                                <div class="col-sm-3">
                                    <input class="form-control" type="date" style="width:300px" name="endDate" id="endDate">
                                </div>

                            </div>
                            <div class="form-group">
                                <button class="btn btn-primary float-right" onclick="enterPress()" type="button">
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
</html>