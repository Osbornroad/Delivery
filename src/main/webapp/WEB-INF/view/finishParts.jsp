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

        function enterPress() {
            save();
        }

        function generateLinkToPart() {
            save();
            var modalId = document.getElementById("id").value;
            window.open("finishPart/" + modalId, "_self");
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
                    <div class="form-row mt-3 pt-3">
<%--                        <div class="form-group">--%>
                            <div class="col" >
                                <a role="button"  id="buttonLinkToOperation" class="btn btn-outline-primary" href="#" onclick=generateLinkToPart()>Part List</a>
                            </div>

<%--                        </div>--%>
<%--                        <div class="form-group">--%>
<%--                            <div class="row">--%>
<%--                                <div class="col-9">--%>
<%--                                    <span id="errorMessage"></span>--%>
<%--                                </div>--%>
                                <div class="col<%---xs-offset-3 col-xs-9--%>">
                                    <button class="btn btn-primary toBeEmpty float-right enter-pressed" type="button" onclick="enterPress()">
                                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                    </button>
                                </div>
<%--                            </div>--%>
<%--                        </div>--%>
                    </div>
                </sf:form>
            </div>

        </div>
    </div>
</div>
</body>
