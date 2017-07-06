/**
 * Created by mylon on 2017/7/3.
 */
$(document).ready(function () {
// 联动城市
    $(".select-country").on("change", function () {
        changeCity();
        queryBdUsers();
    });

    var checkGroupUnique = true;
    $(".name").on("blur", function () {
        var name = $(".name").val();

        var data = {
            name: name,
            id: $(".groupId").val()
        };
        $.ajax({
            url: 'checkGroupUnique',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    if (result.data == false) {
                        checkGroupUnique = false;
                        $(".editGroupModal").text("战队名称重复！");
                        $(".editGroupResultModal").modal("show");
                    } else {
                        checkGroupUnique = true;
                    }
                    console.log(result.data);
                }
            }
        });
    });

    // 编辑队长
    $(".edit-group").on("click", function () {
        if ($(".edit-group").hasClass("disable")) {
            return false;
        }

        var name = $(".name").val();
        var countryId = $(".select-country").val();
        var cityId = $(".select-city").val();
        var leaderId = $(".leader").val();
        var viceLeaderId = $(".viceleader").val();
        var desc = $(".desc").val();

        if (checkGroupUnique == false) {
            showMessModal("战队名称重复！");
            return false;
        }
        if (name == null || name == "") {
            showMessModal("战队名称不能为空！");
            return false;
        }
        if (countryId == null || countryId == "") {
            showMessModal("国家不能为空！");
            return false;
        }
        if (cityId == null || cityId == "") {
            showMessModal("城市不能为空！");
            return false;
        }
        if (leaderId == null || leaderId == "") {
            showMessModal("队长不能为空！");
            return false;
        }
        if (viceLeaderId == null || viceLeaderId == "") {
            showMessModal("副队长不能为空！");
            return false;
        }
        if (viceLeaderId == leaderId) {
            showMessModal("主副队长不能为同一人！");
            return false;
        }
        var data = {
            id: $(".groupId").val(),
            name: name,
            countryId: countryId,
            cityId: cityId,
            leaderId: leaderId,
            viceLeaderId: viceLeaderId,
            desc: desc,
        };
        $(".edit-group").addClass("disable");
        $.ajax({
            url: 'editGroup',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                $(".editGroupResultModal .edit-result").val(result.code);
                $(".edit-group").removeClass("disable");
                if (result.code == 0) {
                    showMessModal("操作成功！")
                } else {
                    showMessModal(result.msg);
                }
            }
        });
    });

    // 添加bduser结果
    $(".editGroupResultModal").on("hide.bs.modal", function () {
        var result = $(".editGroupResultModal .edit-result").val();
        if (result == 0 && result != "") {
            window.location = "list";
        }
    });

    // 联动城市
    function changeCity() {
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
        $(".select-city").val("");
    }


    // 联动bdUser
    function queryBdUsers() {
        var countryId = $(".select-country").val();
        $.ajax({
            url: 'findNonGroupByCountryId',
            data: JSON.stringify({countryId: countryId}),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    $(".leader").empty();
                    $(".leader").append("<option value>请选择队长</option>");
                    $(".viceleader").empty();
                    $(".viceleader").append("<option value>请选择副队长</option>");
                    for (var itemNum in result.data) {
                        var bd = result.data[itemNum]
                        $(".leader").append("<option value='" + bd.id + "'>" + bd.chineseName + "</option>");
                        $(".viceleader").append("<option value='" + bd.id + "'>" + bd.chineseName + "</option>");
                    }
                }
            }
        });
    }

    // 显示提示框
    function showMessModal(msg) {
        $(".editGroupModal").text(msg);
        $(".editGroupResultModal").modal("show");
    }

});


