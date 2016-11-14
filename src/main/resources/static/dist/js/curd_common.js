
$("#update-submit").on("click", function (event) {
    var form=$(this).attr("date-id");
    var isValid=$("#"+form).valid();
    if(!isValid){
        return false;
    }
    $.ajax({
        url: 'update',
        data: $("#"+form).serialize(),
        type: 'post',
        success: function (result) {
            if (result.code == 0) {
                window.location.reload();
            } else {
                alert("失败:" + result.msg);
            }
        }
    });
});

//点击编辑
$(".update-item").each(function (i, o) {
    $(o).on("click", function (event) {
        clear("update-field");
        var dataId = $(this).attr("data-id");
        if (!dataId)
            $("#update-modal").modal();
        else
            $.ajax({
                url: 'detail', data: {id: dataId}, type: 'get',
                success: function (result) {
                    if (result.code == 0) {
                        $(".update-field").each(function (i, o) {
                            var name = $(o).attr('name');
                            if (o.type == "radio") {
                                var v = $(o).val();
                                if (v == result.data[name]) {
                                    $(o).attr("checked", true);
                                    show(v);
                                }
                            } else {
                                $(o).val(result.data[name]);
                            }
                        });
                        $("#update-modal").modal();
                    } else {
                        alert("失败:" + result.msg);
                    }
                }
            });
    });
});



$(".remove-item").each(function (i, o) {
    $(o).on("click", function (event) {
        if (!confirm('确实要删除该内容吗?'))
            return;
        var dataId = $(this).attr("data-id");
        $.ajax({
            url: 'remove',
            data: {id: dataId},
            type: 'post',
            success: function (result) {
                if (result.code == 0) {
                    window.location.reload();
                } else {
                    alert("失败:" + result.msg);
                }
            }
        });
    });
});

function clear(clazz) {
    $("." + clazz).each(function (i, o) {
        if ($(o).attr("class").indexOf("not-clean") == -1) {
            if (o.type != "radio") {
                $(o).val("");
            }
        }
    });
}