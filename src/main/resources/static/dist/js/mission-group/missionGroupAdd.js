/**
 * Created by mylon on 2017/7/2.
 */
$(document).ready(function () {
    changeCity();

    // 战队名称不重复
    var checkGroupUnique = true;
    $(".name").on("blur", function () {
        var name = $(".name").val();

        var data = {
            name: name,
            id: ""
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
                        $(".saveGroupResult").text("战队名称重复！");
                        $(".saveGroupResultModal").modal("show");
                    } else {
                        checkGroupUnique = true;
                    }
                    console.log(result.data);
                }
            }
        });
    });

    // 联动城市
    $(".select-country").on("change", function () {
        changeCity();
        queryBdUsers();
    });

    // 保存组信息
    $(".save-group").on("click", function () {
        if ($(".save-group").hasClass("disable")) {
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
            name: name,
            countryId: countryId,
            cityId: cityId,
            leaderId: leaderId,
            viceLeaderId: viceLeaderId,
            desc: desc,
        };
        $(".save-group").addClass("disable");
        $.ajax({
            url: 'saveGroup',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                $(".saveGroupResultModal .add-result").val(result.code);
                $(".save-group").removeClass("disable");
                if (result.code == 0) {
                    showMessModal("操作成功！")
                } else {
                    showMessModal(result.msg);
                }
            }
        });
    });
    // 添加bduser结果
    $(".saveGroupResultModal").on("hide.bs.modal", function () {
        var result = $(".saveGroupResultModal .add-result").val();
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
        $(".saveGroupResult").text(msg);
        $(".saveGroupResultModal").modal("show");
    }

});



