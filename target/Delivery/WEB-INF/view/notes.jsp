<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

    <head>

        <jsp:include page="fragments/headerTags.jsp"/>

        <script>

            // General scripts

            var table;
            var form;
            var reference;
            var ajaxUrl;
            var referenceName;

            function renderDeleteBtn(data, type, row) {
                var id = row.id;
                if (type == 'display') {
                    return '<a href="#" onclick="deleteRow(' + id + ', \'' + referenceName + '\');">' +
                        '<span class="glyphicon glyphicon-remove" style="color: darkred" aria-hidden="true"></span></a>';
                }
            }

            function deleteRow(id, referenceName) {
                bootbox.confirm({
                    title: "Delete " + reference,
                    message: "Are you sure to delete " + reference + /*" " + referenceName +*/ "?\nAction is irreversible.",
                    callback: function (result) {
                        if (result === true) {
                            $.ajax({
                                url: ajaxUrl + id,
                                type: 'DELETE',
                                success: function () {
                                    table.ajax.reload();
                                },
                                error: function(){
                                    bootbox.alert("You cannot delete this.")
                                }
                            });
                        }
                    }
                });
            }

            function renderEditBtn(data, type, row) {
                if (type == 'display') {
                    return '<a href="#" onclick="openModalEdit(' + row.id + ', \'' + "edit" + '\');">' +
                        '<span class="glyphicon glyphicon-pencil" style="color: blue" aria-hidden="true"></span></a>';
                }
            }

            function openModalEdit(id) {
                clearForm();
                document.getElementById("modalTitle").innerHTML = id === "create" ? "New " + reference : "Edit " + reference;
                $.get(ajaxUrl + id, function (data) {
                    $.each(data, function (key, value) {
                        form.find("input[name='" + key + "']").val(value);
                        form.find("select[name='" + key + "']").val(
                            function () {
                                var currentValue;
                                switch(key) {
                                    case "finishPartSet":
                                        var finishPartArray = [];
                                        var finishPart;
                                        $.each(value, function (i, v) {
                                            finishPart = v.finishPartNumber;
                                            finishPartArray.push(finishPart);
                                        })
                                        currentValue = finishPartArray;
                                        break;
                                    default:
                                        currentValue = value;
                                }
                                return currentValue;
                            }
                        )
                    });
                })
                    .done(function () {
                        if (id === "create") {
                            clearForm();
                            var err = document.getElementById("errorMessage");
                            if (err != null)
                                err.innerHTML = error;
                        }
                        var selectedFPs = document.getElementById("selectedFinishParts");
                        if (selectedFPs != null)
                            selectedFPs.innerHTML = getShowedFinishParts();
                        setInitCheckbox();
                        $('#editRow').modal('show');
                        error = "";
                        $('.finishPartsSelect').on('click', function () {
                            selectedFPs.innerHTML = getShowedFinishParts();
                        });
                    })
                    .fail(function() {
                        bootbox.alert("Something wrong")
                    });
            }

            function makeEditable() {
                form = $('#detailsForm');
            }

            function clearForm() {
                var elements = document.getElementsByClassName("to-empty");
                for (var ii=0; ii < elements.length; ii++) {
                    elements[ii].value = "";
                }
            }

            $(document).ready(function() {
                $(window).keydown(function(event){
                    if(event.keyCode == 13) {
                        // $('#submit').focus();
                        var focused = document.activeElement;
                        if ($(focused).hasClass("enter-pressed")) {
                        } else {
                            event.preventDefault();
                        }
                    }
                });
            });

            function save() {
                error = "";
                $.ajax({
                    type: "POST",
                    url: ajaxUrl,
                    data: form.serialize(),
                    success: function () {
                        $('#editRow').modal('hide');
                        table.ajax.reload();
                        // successNoty('common.saved');
                    },
                    statusCode:{
                        422: function (data) {
                            error422();
                        },
                        406: function (data) {
                            error406();
                        }
                    }
                });
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

            // End of general scripts

            function formatDate(date) {
                var options = {year: 'numeric', month: 'numeric', day: 'numeric', timezone: 'UTC', hour: 'numeric', minute: 'numeric'};
/*                var year = date.year;
                var month = date.monthValue - 1;
                var day = date.dayOfMonth;
                var hour = date.hour;
                var min = date.minute;*/

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

            ajaxUrl = "notes/ajax/";

/*            function updateTable() {
                $('#sendFilter').modal('hide');
                $('#waitingModal').modal('show');
                $.ajax({
                    type: "POST",
                    url: ajaxUrl + "filter",
                    data: $('#filter').serialize(),
                    success: updateTableByData,
                    error: showErrorModal
                });

            }*/

            function updateTableByData(data) {
                table.clear().rows.add(data).draw();
                $('#waitingModal').modal('hide');
            }

            function showErrorModal() {
                $('#waitingModal').modal('hide');
                $('#errorModal').modal('show');
            }

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
                /*var startDate = $('#startDate');
                var endDate = $('#endDate');
                startDate.datetimepicker({
                    timepicker: false,
                    format: 'Y-m-d',
                    formatDate: 'Y-m-d',
                    onShow: function (ct) {
                        this.setOptions({
                            maxDate: endDate.val() ? endDate.val() : false
                        })
                    }
                });
                endDate.datetimepicker({
                    timepicker: false,
                    format: 'Y-m-d',
                    formatDate: 'Y-m-d',
                    onShow: function (ct) {
                        this.setOptions({
                            minDate: startDate.val() ? startDate.val() : false
                        })
                    }
                });*/

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
                                    <input class="form-check-input" type="checkbox" name="A090" id="A090" value="A090">
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
</html>