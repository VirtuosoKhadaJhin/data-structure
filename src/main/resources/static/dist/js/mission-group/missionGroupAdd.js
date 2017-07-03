/**
 * Created by mylon on 2017/7/2.
 */
$(document).ready(function () {
    changeCity();
});

// 联动城市
$(".select-country").on("change", function () {
    changeCity();
    queryBdUsers();
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

// 保存队长
$(".save-group").on("click", function () {
    var name = $(".name").val();
    var countryId = $(".select-country").val();
    var cityId = $(".select-city").val();
    var leaderId = $(".leader").val();
    var viceLeaderId = $(".viceleader").val();
    var desc = $(".desc").val();

    if (name == null || name == "") {
        $(".saveGroupResult").text("战队名称不能为空！");
        $(".saveGroupResultModal").modal("show");
    } else if (countryId == null || countryId == "") {
        $(".saveGroupResult").text("国家不能为空！");
        $(".saveGroupResultModal").modal("show");
    } else if (cityId == null || cityId == "") {
        $(".saveGroupResult").text("城市不能为空！");
        $(".saveGroupResultModal").modal("show");
    } else if (leaderId == null || leaderId == "") {
        $(".saveGroupResult").text("队长不能为空！");
        $(".saveGroupResultModal").modal("show");
    } else if (viceLeaderId == null || viceLeaderId == "") {
        $(".saveGroupResult").text("副队长不能为空！");
        $(".saveGroupResultModal").modal("show");
    } else if (viceLeaderId == leaderId) {
        $(".saveGroupResult").text("主副队长不能为同一人！");
        $(".saveGroupResultModal").modal("show");
    } else {
        var data = {
            name: name,
            countryId: countryId,
            cityId: cityId,
            leaderId: leaderId,
            viceLeaderId: viceLeaderId,
            desc: desc,
        };
        $(".save-group").prop("disabled", true);
        $.ajax({
            url: 'saveGroup',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                $(".saveGroupResultModal .add-result").val(result.code);
                if (result.code == 0) {
                    $(".saveGroupResult").text("编辑成功！");
                    $(".saveGroupResultModal").modal("show");
                }
            }
        });
    }
});

// 添加bduser结果
$(".saveGroupResultModal").on("hide.bs.modal", function () {
    var result = $(".saveGroupResultModal .add-result").val();
    if (result == 0 && result != "") {
        window.location = "list";
    }
});

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

// 邮箱校验
function emailChekc() {
    var email = document.getElementsByName("email")[0].value;
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+\.([a-zA-Z0-9_-])+/;
    var res = reg.test(email);
    if (res == false) {
        alert("邮箱规则错误，请输入正确的邮箱");
    }
    return res;
}