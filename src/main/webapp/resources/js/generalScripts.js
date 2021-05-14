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




