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


                    // var display = data;
                    /*$.each(data, function (i, val) {
                        if(i == 0)
                            display = val.date + "<br>";
                        else
                            display += val.date + "<br>";

                    });*/
                    // var f = (document).getElementById("forecast");
                    // f.innerHTML = display;
                }
            });
        })

        $(document).on('change', '.btn-file :file', function() {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [numFiles, label]);
        });

       /* $(document).ready( function() {
            $('.btn-file :file').on('fileselect', function(event, numFiles, label) {

                var input = $(this).parents('.input-group').find(':text'),
                    log = numFiles > 1 ? numFiles + ' files selected' : label;

                if( input.length ) {
                    input.val(log);
                } else {
                    if( log ) alert(log);
                }

            });
        });

        $(document).ready(function () {
            document.getElementById("files").addEventListener('change', function(event) {
                event.target.webkitEntries.forEach(function(entry) {
                    $('#showDirs').innerHTML = entry;
                    /!* do stuff with the entry *!/
                });
            });
        })*/

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
            <a class="btn btn-outline-info float-right" onclick="tableToExcel('showData', 'W3C Example Table')">Export to Excel</a>
        </div>
    </div>
    <div class="row mb-4">
        <div class="col-12">
<%--            ${forecast}--%>
        <p id='showData'></p>

<%--        <span id="forecast"></span>--%>

           <%-- <c:forEach items="${forecast}" var="item">
                ${item.date};
            </c:forEach>--%>


<%--            <c:forEach items="${forecast}" var="entry">
                Key = ${entry.key}<br>
                &lt;%&ndash;, value = ${entry.value}<br>&ndash;%&gt;
                <c:forEach items="${entry.value}" var="e">
                    ${e.key}, ${e.value}<br>
                </c:forEach>
            </c:forEach>--%>
        </div>
    </div>
</div>
</body>