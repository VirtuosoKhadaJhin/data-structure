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
            } else {
                alert("操作失败，请稍后重试！");
            }

        }
    });
}

function addBdUserModal(countryId, groupId) {
    $(".taskDistributeModel").modal("show");
    $(".taskDistributeModel .hide-groupId").val(groupId);
    var request = {};

    request.groupId = groupId;
    request.countryId = countryId;
    $.ajax({
        url: 'findBdUserByCountryId',
        data: JSON.stringify(request),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                findBdUserByGroupId(groupId);
                var bdUsers = result.data;
                var htmlData = "";
                for (var i = 0; i < bdUsers.length; i++) {
                    var bdUser = bdUsers[i];
                    htmlData += "<label><input class='check-bdUser' style='margin: 10px;margin-top: 8px;' type='checkbox' value='" + bdUser.id + "' />" + bdUser.name + " / " + (bdUser.dmail == null ? "" : bdUser.dmail) + " </label>";
                }
                $("#listAddBd").append(htmlData);
            }
        }
    });
}

function findBdUserByGroupId(groupId) {
    console.log("groupId:" + groupId);
    $.ajax({
        url: 'findBdUserByGroupId',
        data: JSON.stringify({groupId: Number(groupId)}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                var groupBdUsers = result.data;
                var checkBdUserChecks = $(".check-bdUser");
                for (var i = 0; i < checkBdUserChecks.length; i++) {
                    var checkBdUser = checkBdUserChecks[i];
                    for (var j = 0; j < groupBdUsers.length; j++) {
                        var groupBdUser = groupBdUsers[j];
                        if (groupBdUser == $(checkBdUser).val()) {
                            $(checkBdUser).prop("checked", true);
                        }
                    }
                }


            }
        }
    });
}

$(document).ready(function () {
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
    if(isReset){
        $("#city").val("");
    }
}