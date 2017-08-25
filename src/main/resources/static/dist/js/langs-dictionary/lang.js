$(function () {
    $(".tr-class td:not('.td-1,.td-2')").popover(
        {
            animation: true,
            html: true,
            title: "图文消息",
            trigger: "hover",
            delay: "500",
            container: "body"
        }
    );

    $('.tr-class td').on('shown.bs.popover', function () {
        var language = $(this).attr("data-language");
        var imgUrl = $(this).parents("tr").attr("data-img");
        var remark = $(this).parents("tr").attr("data-remark");
        var keyCode = $(this).parents("tr").attr("data-keycode");
        var message = $(this).find(".lang-message-label").attr("data-message");

        var keycodeHtml = "<span class='popover-span'>keycode：</span><label class='popover-label'>" + keyCode + "</label>";
        var messageHtml = "<span class='popover-span'>" + language + "：</span><label class='popover-label'>" + message + "</label>";
        var remarkHtml = "<span class='popover-span'>描述：</span><label class='popover-label'>" + remark + "</label>";
        var imgUrlHtml = "<span class='popover-span'>场景图：</span><img class='popover-img' src='" + imgUrl + "' />";

        $(".popover-content").empty();
        $(".popover-content").append(keycodeHtml).append((message != "" && message != null) ? messageHtml : "").append((remark != "" && remark != null) ? remarkHtml : "").append(imgUrl != "" ? imgUrlHtml : "");
    });

});
/**
 * 通用弹窗
 */
var localeLangs = false;

function langsCommonDialog() {
    $("input[id='keyCode']").val("");
    $("input[data-name='langs']").val("");
    $("#categoryId").val("");
    $("#select2-categoryId-container").html("全部分类");

    $("#keyCodeStatus").hide();
    $("#common_langs_saveBtn").attr("style", "");
    $("#keyCode").css("border", "");
    $("#common_langs_saveBtn").attr("onclick", "saveLangsDictionary()");

    $('#parameterModal').modal('show');
    $("span[dir='ltr']").attr("style", "min-width: 180px;");

    $.ajax({
        url: 'viewLocalLanguage',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                if (result.data != null && localeLangs == false) {
                    localeLangs = true;
                    // 如果不是必填项中文和英文
                    var langsCountry = result.data;
                    if (langsCountry.langsValue != "zh-CN" && langsCountry.langsValue != "en-US") {
                        var localeInputHtml = "<div class='control-group'>"
                            + "<label class='control-label'>"
                            + langsCountry.langsDesc + (langsCountry.langsValue)
                            + "：</label>"
                            + " <div class='controls'>"
                            + " <input name='" + langsCountry.langsCountryKey + "' data-key='" + langsCountry.langsCountryKey + "' type='text' onkeyup='keyCodeRealTime()' data-name='langs' class='span7 countryLanguage'>"
                            + " </div>"
                            + "</div>";
                        $("#langsInput").append(localeInputHtml);

                    }
                }
            }
        }
    });
}

$(".search-reset").click(function () {
    $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
    $(".search-form").find(".select2").val('').trigger('change');
});

/**
 * 验证keyCode是否重复
 */
function verifykeyCode() {
    var request = {};
    var keyCode = $("input[id='verify_keyCode']").val();

    request.keyCode = keyCode;
    $.ajax({
        url: 'verifykeyCode',
        data: JSON.stringify(request),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                if (!result.data) {
                    $("#keyCodeStatus").show();
                    $("#common_langs_saveBtn").attr("onclick", "");
                    $("#keyCode").css("border", "1px solid red");
                    $("#common_langs_saveBtn").attr("style", "background-color: gray;");
                } else {
                    $("#keyCodeStatus").hide();
                    $("#common_langs_saveBtn").attr("style", "");
                    $("#keyCode").css("border", "");
                    $("#common_langs_saveBtn").attr("onclick", "saveLangsDictionary()");
                }
            }
        }
    });
}

/**
 * 检验必填项
 */
function checkRequiredParams() {
    var requiredUS = $("input[name='1']").val();
    var requiredZH = $("input[name='2']").val();
    var categoryId = $("select[name='categoryId']").val();

    if (requiredUS == null || requiredUS == "") {
        $("#requiredKey_1").show();
        $("input[name='1']").css("border", "1px solid red");
        return false;
    } else if (requiredZH == null || requiredZH == "") {
        $("#requiredKey_2").show();
        $("input[name='2']").css("border", "1px solid red");
        return false;
    } else if (categoryId == null || categoryId == "") {
        alert("请选择分类!");
        return false;
    } else {
        $("#requiredKey_1").hide();
        $("input[name='1']").css("border", "");
        $("#requiredKey_2").hide();
        $("input[name='2']").css("border", "");
        return true;
    }
    return true;
}

/**
 * 保存多语言
 */
function saveLangsDictionary() {
    keyCodeRealTime();

    var checkResult = checkRequiredParams();
    if (!checkResult) {
        return false;
    }

    var request = {};

    request.keyCode = $("input[id='keyCode']").val();
    request.categoryId = parseInt($("select[name='categoryId']").val());

    var langsMessageList = [];
    var keyCodes = $("input[data-name='langs']");

    for (var i = 0, j = keyCodes.length; i < j; i++) {
        var keyCodeInput = keyCodes[i];
        var langsKey = parseInt(keyCodeInput.getAttribute("data-key"));
        var message = keyCodeInput.value;
        var langsCountryMessageVo = {langsKey: langsKey, message: message};
        langsMessageList.push(langsCountryMessageVo);
    }

    request.langsMessageList = langsMessageList;

    $("#editBtn").attr("disabled", true);
    $.ajax({
        url: 'saveLangsDictionary',
        data: JSON.stringify(request),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            $("#editBtn").attr("disabled", false);
            if (result.code == 0) {
                alert("操作成功");
                $('#parameterModal').modal('hide');
            } else {
                alert(result.msg);
            }
        }
    });
}

/**
 * 实时更新keyCode展示
 */
function keyCodeRealTime() {
    // 拼接KeyCode
    var requiredUS = $("input[name='1']").val();
    // 如果是英文,则要增加改变事件,用来改变keyCode
    $("input[name='1']").attr("onchange", "keyCodeRealTime()");
    // 选择的分类
    var categoryId = $("select[name='categoryId']").val();
    // keyCode初始化值
    if (requiredUS != null) {
        var categoryName = $("select[name='categoryId']").find("option[value='" + categoryId + "']").text();
        $("input[name='keyCode']").val(categoryName + "_" + requiredUS);
        $("#verify_keyCode").attr("value", categoryName + "_" + requiredUS);
    }
    verifykeyCode();
}

/** ########################多语言common弹出框增加######################## */

/**
 * 获取url中的参数
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

/**
 * 实时保存语言
 * @param obj
 */
function saveMessage(obj) {
    var request = {};
    request.langsKey = $(obj).parent().attr("langs-key");
    request.keyCode = $(obj).parent().attr("key-code");
    request.message = $(obj).val();
    $.ajax({
        url: 'saveMessage',
        data: JSON.stringify(request),
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 0) {
                // alert("操作成功");
            } else {
                alert(result.msg);
            }
        }
    });
    $(obj).parent().html("<label class='lang-message-label' data-message='" + request.message + "'>" + $(obj).val() + "</label>");
}

/**
 * 弹出框修改语言
 */
$("#DataTables_Table_0").delegate(".selectParameter", "click", function (e) {
    $('#parameterModal').modal('show');

});

/**
 * 删除语言
 */
$(".remove-item").each(function (i, o) {
    $(o).on("click", function (event) {
        var keyCode = $(this).attr("key-code");
        if (confirm("确实要删除keyCode为(" + keyCode + ")的多语言记录吗?")) {
            sureRemoveLangsDictionary(keyCode);
        }
    });
});

/**
 * 修改语言
 */
$(".modify-item").each(function (i, o) {
    $(o).on("click", function (event) {
        var keyCode = $(this).attr("key-code");
        var pageIndex = getUrlParam("index");
        if (null == pageIndex) {
            pageIndex = 1;
        }
        location.href = "edit?keyCode=" + keyCode + "&&pageIndex=" + pageIndex;
    });
});

function sureRemoveLangsDictionary(keyCode) {
    if (confirm("确实要删除keyCode为(" + keyCode + ")的多语言记录吗?")) {
        var request = {};
        request.keyCode = keyCode;
        $.ajax({
            url: 'remove',
            data: JSON.stringify(request),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    window.location.reload();
                } else {
                    alert("操作失败:" + result.msg);
                }
            }
        });
    }
}