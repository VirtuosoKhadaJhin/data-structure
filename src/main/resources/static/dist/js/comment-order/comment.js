/**
 * Created by Byron on 2017/7/27.
 */
$(function () {
    $(".detail-item").on("click", function () {
        var modal = $("#" + $(this).attr("modal-id"));
        var id = $(this).attr("data-id");

        $.ajax({
            url: "replies", type: 'get',
            data: {id: id},
            success: function (result) {
                html = "";
                for (var i in result.data) {
                    var reply = result.data[i];
                    html += '<label>' + reply.createTime + '</label>';
                    html += '<textarea class="span10" disabled>' + reply.content + '</textarea>';
                }
                $("#replies").empty().append(html);
                $(modal).modal();
            }
        });
    });

    $(".td-opt .hide-item, .td-opt .show-item").on("click", function () {
        var isShow = true, dataId = $(this).attr("data-id");
        if ($(this).hasClass("hide-item")) {
            isShow = false;
        }
        var isReq = isShow || !isShow && confirm('确实要隐藏此评论吗?');
        if (!isReq) {
            return;
        }
        $.ajax({
            url: 'showOrHideComment',
            type: 'post',
            data: {id: dataId, isShow: isShow},
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
