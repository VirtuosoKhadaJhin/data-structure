/**
 * Created by mylon on 2017/7/3.
 */

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
                    $(".edit-group").attr("disabled", true);
                } else {
                    checkGroupUnique = true;
                    $(".edit-group").attr("disabled", false);
                }
                console.log(result.data);
            }
        }
    });
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

// 编辑队长
$(".edit-group").on("click", function () {
    var name = $(".name").val();
    var countryId = $(".select-country").val();
    var cityId = $(".select-city").val();
    var leaderId = $(".leader").val();
    var viceLeaderId = $(".viceleader").val();
    var desc = $(".desc").val();

    if (checkGroupUnique == false) {
        $(".editGroupModal").text("战队名称重复！");
        $(".editGroupResultModal").modal("show");
    } else if (name == null || name == "") {
        $(".editGroupModal").text("战队名称不能为空！");
        $(".editGroupResultModal").modal("show");
    }
    else if (countryId == null || countryId == "") {
        $(".editGroupModal").text("国家不能为空！");
        $(".editGroupResultModal").modal("show");
    } else if (cityId == null || cityId == "") {
        $(".editGroupModal").text("城市不能为空！");
        $(".editGroupResultModal").modal("show");
    } else if (leaderId == null || leaderId == "") {
        $(".editGroupModal").text("队长不能为空！");
        $(".editGroupResultModal").modal("show");
    } else if (viceLeaderId == null || viceLeaderId == "") {
        $(".editGroupModal").text("副队长不能为空！");
        $(".editGroupResultModal").modal("show");
    } else if (viceLeaderId == leaderId) {
        $(".editGroupModal").text("主副队长不能为同一人！");
        $(".editGroupResultModal").modal("show");
    } else {
        var data = {
            id: $(".groupId").val(),
            name: name,
            countryId: countryId,
            cityId: cityId,
            leaderId: leaderId,
            viceLeaderId: viceLeaderId,
            desc: desc,
        };
        $.ajax({
            url: 'editGroup',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                $(".editGroupResultModal .edit-result").val(result.code);
                if (result.code == 0) {
                    $(".editGroupModal").text("编辑成功！");
                    $(".editGroupResultModal").modal("show");
                }
            }
        });
    }
});

// 添加bduser结果
$(".editGroupResultModal").on("hide.bs.modal", function () {
    var result = $(".editGroupResultModal .edit-result").val();
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