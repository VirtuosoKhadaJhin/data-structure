/**
 * Created by mylon on 2017/7/2.
 */
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
$(document).ready(function () {
    changeCity();
});

// 联动城市
$("#country").on("click", function () {
    changeCity();
});

// 联动城市
function changeCity() {
    var countryId = $("#country").val();
    var cityOptions = $(".option-city");
    for (var i = 0; i < cityOptions.length; i++) {
        var cityOption = cityOptions[i];
        if (countryId != $(cityOption).attr("country-id")) {
            $(cityOption).hide();
        } else {
            $(cityOption).show();
        }
    }
    $("#city").val("");
}


// 保存队长
$(".save-group").on("click", function () {
    $.ajax({
        url: 'saveAdd',
        data: JSON.stringify({groupId: Number(groupId)}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                for (var itemNum in result.data) {
                    // result.data[itemNum]


                }
            }
        }
    });
});

// 联动bdUser
function queryBdUsers() {
    $.ajax({
        url: 'queryBdUsers',
        data: JSON.stringify({groupId: Number(groupId)}),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                for (var itemNum in result.data) {
                    // result.data[itemNum]


                }
            }
        }
    });
}
