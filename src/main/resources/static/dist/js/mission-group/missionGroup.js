/**
 * Created by mylon on 2017/6/28.
 */

// 删除确认弹窗
$('.del-btn').on("click", function () {
    $(".hide-del-id").val($(this).attr("del-id"));
    $(".deleteModal").modal("show");
});

// 搜索框重置
$(".search-reset").click(function () {
    $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
    $(".search-form").find(".select2").val('').trigger('change');
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

// 审批弹窗事件
$('.deleteResultModal').on('hide.bs.modal', function () {
    window.location.reload();
});

// 新增模块
function addUsers() {
    var userIds = new Array();
    $(".check-bdUser:checked").each(function (target) {
        userIds.push($(this).val());
    });
    var groupId = $(".addGroupBdModel .hide-groupId").val();
    var requestParam = {
        groupId: groupId,
        userIds: userIds,
    }
    $.ajax({
        url: 'saveGroupBds',
        data: JSON.stringify(requestParam),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            $(".addGroupBdResultModel .add-result").val(result.code);
            if (result.code == 0) {
                $(".addGroupBdModel").modal("hide");
                $(".addGroupBdResult").text("操作成功！");
                $(".addGroupBdResultModel").modal("show");
            } else {
                $(".addGroupBdModel").modal("hide");
                $(".addGroupBdResult").text("操作失败！");
                $(".addGroupBdResultModel").modal("show");
            }
        }
    });
}

// 添加BD弹窗
function addBdUserModal(countryId, groupId) {
    $.ajax({
        url: 'findBdUserByCountryId',
        data: JSON.stringify({"groupId": groupId, "countryId": countryId}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                var bdUsers = result.data;
                var htmlData = "";
                for (var i = 0; i < bdUsers.length; i++) {
                    var bdUser = bdUsers[i];
                    htmlData += "<label><input class='check-bdUser' style='margin: 10px;margin-top: 8px;' type='checkbox' data-key='" + bdUser.id + "' value='" + bdUser.id + "' />" + bdUser.name + " / " + (bdUser.dmail == null ? "" : bdUser.dmail) + " </label>";
                }
                $("#listAddBd").html(htmlData);
                $(".addGroupBdModel").modal("show");
                $(".addGroupBdModel .hide-groupId").val(groupId);
            }
        }
    });
}

// 添加bduser结果
$(".addGroupBdResultModel").on("hide.bs.modal", function () {
    var result = $(".addGroupBdResultModel .add-result").val();
    if (result == 0 && result != "") {
        window.location.reload();
    }
});

// 指定bdUser为组长
$(".distributeLeader").on("click", function () {
    var groupId = $(".distributeLeaderModal .hide-groupId").val();
    var leaderId = $(".distributeLeaderModal .hide-leaderId").val();

    $.ajax({
        url: 'distributeLeader',
        data: JSON.stringify({"groupId": groupId, "leaderId": leaderId}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                $(".distributeLeaderResult").text("指定成功");
                $(".distributeLeaderModal").modal("hide");
                $(".distributeLeaderResultModal").modal("show");
            } else {
                $(".distributeLeaderResult").text("指定失败");
                $(".distributeLeaderModal").modal("hide");
                $(".distributeLeaderResultModal").modal("show");
            }
        }
    });
});

/**
 * 指定组长弹窗
 * @param groupId
 * @param countryId
 */
function distributeLeaderModal(groupId, countryId) {
    $.ajax({
        url: 'queryGroupBdUsers',
        data: JSON.stringify({"groupId": groupId, "countryId": countryId}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                var bdUsers = result.data;
                var htmlData = "";
                if (bdUsers == null) {
                    htmlData = "<strong class='deleteResult' style='margin-left: 20px;font-size: 14px;'>请分配组员</strong>";
                } else {
                    for (var i = 0; i < bdUsers.length; i++) {
                        var bdUser = bdUsers[i];
                        if (bdUser.isLeader) {
                            htmlData += "<label><input class='check-bdUser-leader' style='margin: 10px;margin-top: 8px;' type='checkbox' data-key='" + bdUser.id + "' value='" + bdUser.id + "' checked='checked' />" + bdUser.name + " / " + (bdUser.dmail == null ? "" : bdUser.dmail) + " </label>";
                        } else {
                            htmlData += "<label><input class='check-bdUser-leader' style='margin: 10px;margin-top: 8px;' type='checkbox' data-key='" + bdUser.id + "' value='" + bdUser.id + "' />" + bdUser.name + " / " + (bdUser.dmail == null ? "" : bdUser.dmail) + " </label>";
                        }
                    }
                }
                $("#listGroupLeaderAddBd").html(htmlData);
                $(".distributeLeaderModal").modal("show");
                $(".distributeLeaderModal .hide-groupId").val(groupId);
            }
            // Leader单选
            $(".check-bdUser-leader").on("click", function () {
                $(".distributeLeaderModal :checkbox").removeAttr("checked");
                $(this).prop("checked", true);
                $(".distributeLeaderModal .hide-leaderId").val($(this).attr("data-key"));
            });
        }
    });
}

$(document).ready(function () {
    $(".addGroupBdModel").on("shown.bs.modal", function () {
        var groupId = $(".addGroupBdModel .hide-groupId").val();
        $.ajax({
            url: 'findBdUserByGroupId',
            data: JSON.stringify({groupId: Number(groupId)}),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    console.log($(".addGroupBdModel .check-bdUser").length);
                    for (var itemNum in result.data) {
                        $(".addGroupBdModel .check-bdUser[data-key=" + result.data[itemNum] + "]").prop("checked", true);
                    }
                }
            }
        });
    })

    changeCity(false);
});
$(".select-country").on("change", function () {
    changeCity(true);
});
function changeCity(isReset) {
    var countryId = $(".select-country").val();
    var cityOptions = $(".option-city");
    for (var i = 0; i < cityOptions.length; i++) {
        var cityOption = cityOptions[i];
        if (countryId != $(cityOption).attr("country-id")) {
            $(cityOption).hide();
        } else {
            $(cityOption).show();
        }
    }
    if (isReset) {
        $("#city").val("");
    }
}