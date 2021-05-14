<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>

<jsp:include page="fragments/headerTags.jsp"/>

    <style>
        td.details-control {
            background: url("${pageContext.request.contextPath}/resources/images/details_open.png") no-repeat center center;
            cursor: pointer;
        }
        tr.shown td.details-control {
            background: url("${pageContext.request.contextPath}/resources/images/details_close.png") no-repeat center center;
        }
    </style>
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

        ajaxUrl = "kits/ajax/";
        reference = "Kit";

        $(document).ready(function () {
            table = $('#kitsTable').DataTable({
                ajax : {
                    url : ajaxUrl,
                    dataSrc : ""
                },
                columns : [
                    {
                        className: 'details-control',
                        orderable: false,
                        data: null,
                        defaultContent: ''
                    },
                    {
                        data: "kitName"
                    },
                    {
                        data: "wib224"
                    },
                    {
                        data: "series"
                    },
                    {
                        data: "sortNum"
                    },
                    {
                        data: "current",
                        render: renderCheckBox
                    },
                    {
                        render: renderEditBtn,
                        defaultContent: "",
                        orderable: false
                    },
                    {
                        render: renderDeleteBtn,
                        defaultContent: "",
                        orderable: false
                    }
                ],
                initComplete: makeEditable
            });

            // Add event listener for opening and closing details
            $('#kitsTable tbody').on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table.row( tr );

                if ( row.child.isShown() ) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    // Open this row
                    row.child( format(row.data()) ).show();
                    tr.addClass('shown');
                }
            } );
        });


        function renderCheckBox(data, type, row) {
            if (data.toString() === "true")
                return '<span class="glyphicon glyphicon-check" aria-hidden="true"></span>';
            else
                return '<span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span>';
        }

        function clickCheckbox() {
            var checkbox = document.getElementById("checkboxCurrent");
            var current = document.getElementById("current");
            if (checkbox.checked) {
                current.value = "true";
            } else {
                // $('#checkboxEnabled').prop('checked', true);
                // $('#alertDisable').show();
                current.value = "false";
            }
        }

        function setInitCheckbox() {
            // var checkbox = document.getElementById("checkboxCurrent");
            var current = document.getElementById("current");
            // var currentVal = current.value;
            if (current.value == "true")
                $("#checkboxCurrent").prop("checked", true);
                // checkbox.checked = "true";
            else
                $("#checkboxCurrent").prop("checked", false);
            // checkbox.checked = "false";
        }

        function format ( d ) {
            var jobs= d.finishPartSet;
            var childTable = '<table  style="padding-left:20px; margin-left: 20px">'+
                '<tr>' +
                '<th style="width: 200px">Finish Parts</th>' +
                '</tr>';
            jobs.forEach(function (item, i, arr)
            {
                childTable = childTable +
                    '<tr>' +
                    '<td>' + item.finishPartNumber + '</td>' +
                    '</tr>';
            });
            return childTable;
        }

/*        function save() {
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
                        // error = "Please input part number.";
                        // openModalEdit("create")
                    },
                    406: function (data) {
                        // error = "Entered part number already exists.";
                        // openModalEdit("create")
                    }
                }
            });
        }*/

        function error422() {
        }

        function error406() {
        }

        function getShowedFinishParts() {
            var finishParts = $('#finishPartSet').val();
            var  text, arrLen, i;
            arrLen = finishParts.length;

            text = '<ul class=\"list-group\">';
            for (i = 0; i < arrLen; i++) {
                text += '<li class=\"list-group-item py-1 pl-3\">' + finishParts[i] + "</li>";
            }
            text += "</ul>";

            return text;
        }

        $(document).ready(function() {
            $('#editRow').on('shown.bs.modal', function() {
                $('#kitName').trigger('focus');
            });
        });

    </script>

</head>
<body>
    <div class="container float-left mt-4">
        <div class = "row" style="height: 50px">
            <jsp:include page="fragments/navbar.jsp"/>

        </div>
        <div class="row mb-4">
            <div class="col-5">
                <h4>Kits</h4>
            </div>
            <div class="col-5">
                <a class="btn btn-outline-info float-right" onclick="openModalEdit('create')"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
            <%--        <div class="col text-left">
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#sendFilter">Filter</button>
                    </div>--%>
        </div>
        <div class="row">
            <div class="col-10">
                <table class="table table-hover table-striped display table-sm small" id="kitsTable">
                    <thead class="thead-light">
                    <tr>
                        <th style="width:10px"></th>
                        <th >Kit name</th>
                        <th >WIB 224</th>
                        <th >Series</th>
                        <th >Sort number</th>
                        <th >Current</th>
                        <th style="width:30px"></th>
                        <th class="text-center" style="width:30px"></th>
                    </tr>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editRow">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title" id="modalTitle"></h2>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body">
                    <sf:form class="form-horizontal" id="detailsForm">
                        <input type="hidden" id="id" name="id" class="to-empty">
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="kitName" class="control-label col-xs-3">Kit name</label>
                                <div class="col-xs-9">
                                    <input class="form-control to-empty" id="kitName" name="kitName"
                                           placeholder="Input Kit name">
                                </div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="wib224" class="control-label col-xs-3">Wib 224</label>
                                <div class="col-xs-9">
                                    <select class="form-control to-empty" id="wib224" name="wib224">
                                        <c:forEach var="item" items="${wib224}">
                                            <option value="${item}">${item}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="series" class="control-label col-xs-3">Series</label>
                                <div class="col-xs-9">
                                    <select class="form-control to-empty" id="series" name="series">
                                        <c:forEach var="item" items="${series}">
                                            <option value="${item}">${item}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="sortNum" class="control-label col-xs-3">Sort number</label>
                                <div class="col-xs-9">
                                    <input class="form-control to-empty" id="sortNum" name="sortNum"
                                           placeholder="Input sort number">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-4">
                                <div class="checkbox" onclick="clickCheckbox()">
                                    <label id="labelForEnabled"><input type="checkbox" id="checkboxCurrent"<%-- name="checkbox"--%>> Current project</label>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" id="current" name="current"<%-- class="to-empty"--%>>
                        <div class="form-group">
                            <label for="selectedFinishParts" class="control-label col-xs-3">Finish parts</label>
                            <div class="col-xs-9">
                                <span id="selectedFinishParts"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="finishPartSet" class="control-label col-xs-3">Select finish parts holding "Ctrl"</label>
                            <div class="col-xs-9">
                                <select multiple class="finishPartsSelect form-control to-empty not-empty" id="finishPartSet" name="finishPartSet">
                                    <c:forEach var="item" items="${finishParts}">
                                        <option value="${item.finishPartNumber}">${item.finishPartNumber}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col<%---xs-offset-3 col-xs-9--%>">
                                <button class="btn btn-primary toBeEmpty float-right enter-pressed" type="button" onclick="save()">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </sf:form>
                </div>

            </div>
        </div>
    </div>

</body>