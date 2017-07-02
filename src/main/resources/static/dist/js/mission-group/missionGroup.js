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
    console.log(JSON.stringify(data));
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

function addUsers() {
    var userIds = new Array();
    $(".check-bdUser:checked").each(function (target) {
        userIds.push($(this).val());
    });
    var groupId = $(".taskDistributeModel .hide-groupId").val();
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
            if (result.code == 0) {
                alert("操作成功！")
                $(".taskDistributeModel").modal("hide");
            } else {
                alert("操作失败，请稍后重试！");
            }

        }
    });
}

/**
 * 指定组长弹窗
 *
 * @param groupId
 */
function distributeLeaderModal(groupId) {
    console.log(groupId);
    $.ajax({
        url: 'members',
        data: JSON.stringify({"groupId": groupId}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                var bdUsers = result.data;
                var htmlData = "";
                if (bdUsers.length == 0) {
                    htmlData = "<strong class='deleteResult' style='margin-left: 20px;font-size: 14px;'>请分配组员</strong>";
                }
                for (var i = 0; i < bdUsers.length; i++) {
                    var bdUser = bdUsers[i];
                    if (bdUser.isLeader) {
                        htmlData += "<label><input class='check-bdUser-leader' style='margin: 10px;margin-top: 8px;' type='checkbox' data-key='" + bdUser.id + "' value='" + bdUser.id + "' checked='checked' />" + bdUser.name + " / " + (bdUser.dmail == null ? "" : bdUser.dmail) + " </label>";
                    }else{
                        htmlData += "<label><input class='check-bdUser-leader' style='margin: 10px;margin-top: 8px;' type='checkbox' data-key='" + bdUser.id + "' value='" + bdUser.id + "' />" + bdUser.name + " / " + (bdUser.dmail == null ? "" : bdUser.dmail) + " </label>";
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

// 指定bdUser为组长
$(".distributeLeader").on("click", function () {
    var groupId = $(".distributeLeaderModal .hide-groupId").val();
    var leaderId = $(".distributeLeaderModal .hide-leaderId").val();

    console.log("组id：" + groupId);
    console.log("队长id：" + leaderId);

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
            }else{
                $(".distributeLeaderResult").text("指定失败");
                $(".distributeLeaderModal").modal("hide");
                $(".distributeLeaderResultModal").modal("show");
            }
        }
    });

});

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
                $(".taskDistributeModel").modal("show");
                $(".taskDistributeModel .hide-groupId").val(groupId);
            }
        }
    });
}

$(document).ready(function () {
    $(".taskDistributeModel").on("shown.bs.modal", function () {
        var groupId = $(".taskDistributeModel .hide-groupId").val();
        $.ajax({
            url: 'findBdUserByGroupId',
            data: JSON.stringify({groupId: Number(groupId)}),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    for (var itemNum in result.data) {
                        $(".taskDistributeModel .check-bdUser[data-key=" + result.data[itemNum] + "]").prop("checked", true);
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