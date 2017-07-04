/**
 * Created by mylon on 2017/7/2.
 */

// 重置搜索框
$(".search-reset").click(function () {
    $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
    $(".search-form").find(".select2").val('').trigger('change');
});

// 删除确认弹窗
$(".del-btn").on("click", function () {
    $(".hide-del-id").val($(this).attr("del-id"));
    $(".deleteModal").modal("show");
});

// 二次删除确认窗
$('.sure-del').on("click", function () {
    $(".deleteModal").modal("hide");
    $(".secondDeleteModal").modal("show");
});

// 删除
$('.second-sure-del').on("click", function () {
    var id = $(".hide-del-id").val();
    var data = {id: Number(id)};
    $.ajax({
        url: 'del',
        data: JSON.stringify(data),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            $(".secondDeleteModal").modal("hide");
            if (result.code == 0) {
                $(".deleteResult").text("删除成功");
            } else {
                $(".deleteResult").text(result.msg);
            }
            $(".deleteResultModal").modal("show");
        }
    });
});

// 删除完成刷新页面
$(".deleteResultModal").on("hide.bs.modal", function () {
    window.location.reload();
});