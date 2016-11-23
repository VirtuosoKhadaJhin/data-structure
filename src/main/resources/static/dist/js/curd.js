$(".update-item").each(function (i, o) {
    $(o).on("click", function () {
        var modal = $("#" + $(this).attr("modal-id"));
        var form = $(modal).find("form")[0];
        form.reset();

        var url = $(this).attr("action-get");
        if (!url)
            url = "detail";

        var dataId = $(this).attr("data-id");
        if (!dataId)
            $(modal).modal();
        else
            $.ajax({
                url: url, type: 'get', data: {id: dataId},
                success: function (result) {
                    if (result.code == 0) {
                        $(form).find(":input").each(function (i, o) {
                            var ignore = $(o).attr("ignore");
                            if (ignore)
                                return;
                            var name = $(o).attr('name');
                            var value = getValue(result.data, name);
                            if (o.type == "radio") {
                                var v = $(o).val();
                                if (v == value + "") {
                                    $(o).attr("checked", true);
                                    if (isExitsFunction("show"))
                                        show(v);
                                } else {
                                    $(o).attr("checked", false);
                                }
                            } else {
                                $(o).val(value);
                                if (o.multiple == true)
                                    $(o).trigger("change");
                            }
                        });
                        if (isExitsFunction("callback"))
                            callback(result);
                        $(modal).modal();
                    } else {
                        alert(result.msg);
                    }
                }
            });
    });
    function getValue(data, name) {
        if (!name)
            return null;
        var names = name.split('.');
        for (var i in names) {
            data = data[names[i]];
        }
        return data;
    }
});


$(".add-item").each(function (i, o) {
    $(o).on("click", function () {
        var modal = $("#" + $(this).attr("modal-id"));
        var form = $(modal).find("form")[0];
        form.reset();

        var name = $(this).attr("data-name");
        var value = $(this).attr("data-value");
        var inp = $(form).find("input[name='" + name + "']")[0];
        $(inp).val(value);
        $(modal).modal();
    });
});

$(".submit-item").on("click", function () {
    var modal = $("#" + $(this).attr("modal-id"));
    var form = $(modal).find("form")[0];
    $.ajax({
        url: $(form).attr('action'), type: 'post',
        data: $(form).serialize(),
        success: function (result) {
            if (result.code == 0) {
                window.location.reload();
            } else {
                alert(result.msg);
            }
        }
    });
});

$(".remove-item").each(function (i, o) {
    $(o).on("click", function () {
        if (!confirm('确实要删除该内容吗?'))
            return;
        var dataId = $(this).attr("data-id");
        $.ajax({
            url: 'remove', type: 'post', data: {id: dataId},
            success: function (result) {
                if (result.code == 0) {
                    window.location.reload();
                } else {
                    alert(result.msg);
                }
            }
        });
    });
});

function isExitsFunction(funcName) {
    try {
        if (typeof(eval(funcName)) == "function") {
            return true;
        }
    } catch (e) {
    }
    return false;
}