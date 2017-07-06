/**
 * Created by mylon on 2017/7/3.
 */
$(document).ready(function () {
    var sureChangeCountry = false;

    changeCity();

    // 联动城市
    $(".select-country").on("change", function () {
        // 确认是否改变国家
        var newCountryId = $(".select-country").val();
        var groupCountryId = $(".groupCountryId").val();

        if (newCountryId != groupCountryId) {
            $(".editGroupCountry").text("更换国家会默认解除当前组成员及队长职位，确认要继续吗？");
            $(".editGroupCountryModal").modal("show");
        } else {
            changeCity();
            queryBdUsers();
        }
    });

    // 取消更换国家
    $(".cancel-change-country").on("click", function () {
        var groupCountryId = $(".groupCountryId").val();
        $(".select-country").val(groupCountryId);
    });

    // 确认更换国家
    $(".sure-change-country").on("click", function () {
        changeCity();
        queryBdUsers();
        $(".editGroupCountryModal").modal("hide");
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
                }
            }
        });
    });

    // 编辑的时候再次确认是否更换国家
    $(".second-sure-change-country").on("click", function () {
        sureChangeCountry = true;
        $(".secondEditGroupCountryModal").modal("hide");
        $(".edit-group").click();
    });

    // 编辑队长
    $(".edit-group").on("click", function () {
        if ($(".edit-group").hasClass("disable")) {
            return false;
        }

        // 优先判断是否更换城市
        var newCountryId = $(".select-country").val();
        var groupCountryId = $(".groupCountryId").val();

        if (newCountryId != groupCountryId) {
            if (!sureChangeCountry) {
                $(".secondEditGroupCountryModal").modal("show");
                return false;
            }
        } else {
            sureChangeCountry = true;
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


