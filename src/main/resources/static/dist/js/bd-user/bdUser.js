/**
 * Created by mylon on 2017/7/2.
 */
$(document).ready(function () {

    var validUserName = true;

    var emailReg = /^\w[-_\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;

    // 新增/编辑 用户
    $(".btn-ok").click(function () {
        if (!validUserName) {
            $(".updateBdUserResult").text("用户名重复！");
            $(".updateBdUserResultModal").modal("show");
            return false;
        }
        if ($("input.bd-name").val().length == 0) {
            $(".updateBdUserResult").text("用户名不能为空！");
            $(".updateBdUserResultModal").modal("show");
            return false;
        }
        if (!emailReg.test($(".bd-email").val())) {
            $(".updateBdUserResult").text("合同归档邮箱格式不正确！");
            $(".updateBdUserResultModal").modal("show");
            return false;
        }
        if (!emailReg.test($(".bd-dmail").val())) {
            $(".updateBdUserResult").text("钉钉邮箱格式不正确！");
            $(".updateBdUserResultModal").modal("show");
            return false;
        }
        var requestParam = {
            id: $(".hide-user-id").val(),
            name: $(".bd-name").val(),
            chineseName: $(".bd-chinese-name").val(),
            countryId: $(".bd-country-id").val(),
            email: $(".bd-email").val(),
            dmail: $(".bd-dmail").val(),
            roleId: $(".bd-role").val(),
        };

        $.ajax({
            url: 'saveBdUser',
            data: JSON.stringify(requestParam),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                $(".save-bd-result").val(result.code);
                if (result.code == 0) {
                    $(".updateBdUserResult").text("操作成功！");
                    $(".updateBdUserResultModal").modal("show");
                } else {
                    $(".updateBdUserResult").text("操作失败,请稍后重试！");
                    $(".updateBdUserResultModal").modal("show");
                }
            }
        });
    });

    $('.updateBdUserResultModal').on('hide.bs.modal', function () {
        if ($(".save-bd-result").val() == 0 && $(".save-bd-result").val() != "") {
            window.location = "list";
        }
    });


    $(".bd-name").on("change", function () {
        var name = $(".bd-name").val();
        var data = {id: $(".hide-user-id").val(), name: name};
        $.ajax({
            url: 'checkUserUnique',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.data) {
                    validUserName = false;
                    $(".updateBdUserResult").text("用户名重复！");
                    $(".updateBdUserResultModal").modal("show");
                } else {
                    validUserName = true;
                }
            }
        });
    });

    $(".mouseout-check").on("blur", function () {
        if (!emailReg.test($(this).val())) {
            var labelText = $(this).parent().prev().text();
            labelText = labelText.replace("：", "");
            $(".updateBdUserResult").text(labelText + "格式不正确！");
            $(".updateBdUserResultModal").modal("show");
        }
    });

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

});