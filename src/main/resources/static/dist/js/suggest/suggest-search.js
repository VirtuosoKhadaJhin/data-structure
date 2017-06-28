$(function () {
    var listComplete = [];
    $(".langsAutoComplete").bind("keyup", function (e) {
        var currEle = e.currentTarget;
        var keyWord = $(this).val();
        if (!keyWord) {
            return false;
        }
        var obj = $(this);
        $.ajax({
            url: '/langsDictionary/suggest',
            data: {"key": keyWord},
            type: 'post',
            dataType: 'json',
            async: false,
            success: function (result) {
                if (result.code == 0) {
                    var list = result.data;
                    if (list.length == 0) {
                        $(currEle).next().val("");
                        return false;
                    }
                    var complate = {};
                    for (var o in list) {
                        complate.labelDisplay = list[o].message + "(" + list[o].keyCode + ")";
                        complate.label = list[o].message + "(" + list[o].keyCode + ")";
                        complate.value = list[o].keyCode;
                        listComplete.push(complate);
                        complate = {};
                    }
                } else {
                    alert(result.msg);
                }
            }
        });
    }).autocomplete({
        source: listComplete,
        focus: function (event, ui) {
            $(this).val(ui.item.value);
            console.log(ui);
            $(this).next().val(ui.item.value);
            return false;

        },
        select: function (event, ui) {
            $(this).val(ui.item.labelDisplay);
            console.log(ui);
            $(this).next().val(ui.item.value);
            return false;
        }
    });
});
