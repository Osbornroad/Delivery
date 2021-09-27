<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>

    <jsp:include page="fragments/headerTags.jsp"/>

    <script>

        ajaxUrl = "${finishPartId}/ajax/";
        reference = "Part in FP";
        referenceName = "ref name";
        var error = "";

        $(document).ready(function () {
            table = $('#partsTable').DataTable({
                ajax : {
                    url : ajaxUrl,
                    dataSrc : ""
                },
                columns : [
                    {
                        data: "partId"
                    },
                    {
                        data: "partNumber",
/*                        render: function (partId, type, row) {
                            if (type === 'display') {
                                return getPartNumber(partId);
                            }
                            return partId;
                        }*/
                    },
                    {
                        data: "qty"
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

        function renderDeleteBtn(data, type, row) {
            var id = row.partId;
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
                return '<a href="#" onclick="openModalEdit(' + row.partId + ', \'' + "edit" + '\');">' +
                    '<span class="glyphicon glyphicon-pencil" style="color: blue" aria-hidden="true"></span></a>';
            }
        }

        function openModalEdit(id) {
            clearForm();
            document.getElementById("modalTitle").innerHTML = id === "create" ? "New " + reference : "Edit " + reference;
            $.get(ajaxUrl + id, function (data) {
                $.each(data, function (key, value) {
                    form.find("input[name='" + key + "']").val(value);
                    form.find("select[name='" + key + "']").val(value);
                });
            })
                .done(function () {
                    if (id !== "create") {
                        $('#partId').prop("disabled", true);

                    } else {
                        $('#partId').prop("disabled", false);
                    }
                    $('#editRow').modal('show');
                })
                .fail(function() {
                    bootbox.alert("Something wrong")
                });
        }

        function getPartNumber(partId) {
            var parts = JSON.parse('${allParts}');
            var partName = "no name";
            parts.forEach((part) => {
                if (part.id === partId) {
                    partName = part.partNumber;
                }
            });
            /*var first = partName.substr(0, 5);
            var second = partName.substr(5, 5);
            if (partName.length === 10) {
                partName = first + "-" + second;
            } else if (first === "17507") {
                // second = partName.substr(5, 10);
                var third = partName.substr(10);
                partName = first + "-" + second + "-" + third;
            }*/

            return partName;
        }

        function setInitCheckbox() {
        }

        function enterPress() {
            $('input, select').prop('disabled', false);
            save();
        }

        // define a handler
        function doc_keyUp(e) {

            // this would test for whichever key is 40 (down arrow) and the ctrl key at the same time
            if (e.shiftKey && e.keyCode === 68) {
                // call your function to do the thing
                openModalEdit('create');
            }
        }
        // register the handler
        document.addEventListener('keyup', doc_keyUp, false);

    </script>


</head>

<body>

<div class="container float-left mt-4">
    <div class = "row" style="height: 50px">
        <jsp:include page="fragments/navbar.jsp"/>

    </div>
    <div class="row mb-4">
        <div class="col-5">
            <h4>${finishPartNumber}</h4>
        </div>
        <div class="col-5">
            <a class="btn btn-outline-info float-right" onclick="openModalEdit('create')"><span class="glyphicon glyphicon-plus"></span></a>
        </div>
    </div>
    <div class="row">
        <div class="col-10">
            <table class="table table-hover table-striped display table-sm small" id="partsTable">
                <thead class="thead-light">
                <tr>
                    <th class="p-1 align-middle text-left" style="width: 60px">Part ID</th>
                    <th class="p-1 align-middle text-left" >Part number</th>
                    <th class="p-1 align-middle text-left" >Quantity</th>
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
                    <input type="hidden" id="finishPartId" name="finishPartId" class="to-empty">
                    <div class="form-group">
                        <label for="partId" class="control-label col-xs-3">Child part number</label>
                        <div class="col-xs-9">
                            <select class="form-control to-empty" id="partId" name="partId" autofocus>
                                <c:forEach var="item" items="${partList}">
                                    <option value="${item.id}">${item.partNumber}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="qty" class="control-label col-xs-3">Quantity</label>
                        <div class="col-xs-9">
                            <input class="form-control to-empty" id="qty" name="qty"
                                   <%--placeholder="Input quantity"--%> value="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-9">
                                <span id="errorMessage"></span>
                            </div>
                            <div class="col<%---xs-offset-3 col-xs-9--%>">
                                <button class="btn btn-primary toBeEmpty float-right enter-pressed" type="button" onclick="enterPress()">
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
