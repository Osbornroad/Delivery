<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>

    <jsp:include page="fragments/headerTags.jsp"/>
    <style>
        #errorMessage {
            color: red;
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
                success: function (data, textStatus) {
                    if (data.redirect) {
                        return;
                    }
                    $('#editRow').modal('hide');
                    table.ajax.reload();
                    // successNoty('common.saved');
                },
                statusCode:{
/*                    422: function (data) {
                        error422();
                    },
                    406: function (data) {
                        error406();
                    },
                    302: function() {
                        alert( "access denied" );
                    },*/
/*                    403: function() {
                        $('#editRow').modal('hide');
                        alert( "Access denied" );
                    }*/
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

        ajaxUrl = "finishParts/ajax/";
        reference = "Finish Part";
        var error = "";

        $(document).ready(function () {
            table = $('#finishPartsTable').DataTable({
                ajax : {
                    url : ajaxUrl,
                    dataSrc : ""
                },
                columns : [
                    {
                        data: "id"
                    },
                    {
                        data: "finishPartNumber"
                    },
                    {
                        data: "sortNum"
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
        });

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
                        error422();
                    },
                    406: function (data) {
                        error406();
                    }
                }
            });
        }*/

        function error422() {
            error = "Please input part number.";
            openModalEdit("create")
        }

        function error406() {
            error = "Entered part number already exists.";
            openModalEdit("create")
        }

        $('#editRow').on('shown.bs.modal', function () {
            $('#finishPartNumber').trigger('focus')
        })

        function setInitCheckbox() {
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
            <h4>Finish Parts</h4>
        </div>
        <div class="col-5">
            <a class="btn btn-outline-info float-right" onclick="openModalEdit('create')"><span class="glyphicon glyphicon-plus"></span></a>
    </div>
    <div class="row">
        <div class="col-10">
            <table class="table table-hover table-striped display table-sm small" id="finishPartsTable">
                <thead class="thead-light">
                <tr>
                    <th class="p-1 align-middle text-left" style="width: 60px">ID</th>
                    <th class="p-1 align-middle text-left" >Finish Part number</th>
                    <th class="p-1 align-middle text-left" >Sort number</th>
                    <th style="width:30px"></th>
                    <th class="text-center" style="width:30px"></th>
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
                    <div class="form-group">
                        <label for="finishPartNumber" class="control-label col-xs-3">Finish part number</label>
                        <div class="col-xs-9">
                            <input class="form-control to-empty" id="finishPartNumber" name="finishPartNumber"
                                   placeholder="Input Finish part number">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sortNum" class="control-label col-xs-3">Sort number</label>
                        <div class="col-xs-9">
                            <input class="form-control to-empty" id="sortNum" name="sortNum"
                                   placeholder="Input Sort number">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-9">
                                <span id="errorMessage"></span>
                            </div>
                            <div class="col<%---xs-offset-3 col-xs-9--%>">
                                <button class="btn btn-primary toBeEmpty float-right enter-pressed" type="button" onclick="save()">
                                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>
                </sf:form>
            </div>

        </div>
    </div>
</div>
</body>
