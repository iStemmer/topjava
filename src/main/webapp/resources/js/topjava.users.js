// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "ajax": {
                    "url": userAjaxUrl,
                    "dataSrc": ""
                },
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email",
                        "render": function (data, type, row) {
                            if (type === "display") {
                                return "<a href='mailto:" + data + "'>" + data + "</a>";
                            }
                            return data;
                        }
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled",
                        "render": function (data, type, row) {
                            if (type === "display") {
                                return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='enable($(this)," + row.id + ");'/>";
                            }
                            return data;
                        }
                    },
                    {
                        "data": "registered",
                        "render": function (date, type, row) {
                            if (type === "display") {
                                return date.substring(0, 10);
                            }
                            return date;
                        }

                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false,
                        "defaultContent": "",
                        "render": renderEditBtn
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false,
                        "defaultContent": "",
                        "render": renderDeleteBtn
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ],
                "createdRow": function (row, data, dataIndex) {
                    if (!data.enabled) {
                        $(row).attr("data-userEnabled", false);
                    }
                }
            }),
            updateTable: function () {
                $.get("ajax/admin/users/", updateTableByData);
            }
        }
    );
});