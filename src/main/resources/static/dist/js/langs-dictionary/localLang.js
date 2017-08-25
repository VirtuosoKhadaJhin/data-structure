/**
 * 实时保存语言
 * @param obj
 */
function saveLang(obj) {
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
            } else {
                alert(result.msg);
            }
        }
    });
    $(obj).parent().html("<label class='lang-message-label' data-message='" + request.message + "'>" + $(obj).val() + "</label>");
}

$(function () {
    /**
     * 切换国家
     */
    $(".local-country-select").on("change", function () {
        var countryKey = $(".local-country-select").val();
        var url = "localList";
        if (countryKey != "") {
            url += "?countryKey=" + countryKey;
        }
        location.href = url;
    });

    /**
     * 修改语言
     */
    $(".modify-item").each(function (i, o) {
        $(o).on("click", function (event) {
            var countryKey = $(".country-key").val();
            var keyCode = $(this).attr("key-code");
            if (countryKey == 0) {
                countryKey = $('.local-country-select>option:nth-child(2)').val();
            }
            if (countryKey == undefined) {
                location.href = "localEdit?keyCode=" + keyCode;
                return true;
            }
            location.href = "localEdit?keyCode=" + keyCode + "&&countryKey=" + countryKey;
        });
    });

    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });

});

$(function () {
    $(".tr-class td").popover(
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