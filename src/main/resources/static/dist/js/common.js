window.onload = function () {
    $('.dateTimeFormat').datetimepicker({
        format: "Y-m-d H:i:s",
        timepicker: true,
        yearStart: 2000,
        yearEnd: 2050,
        todayButton: false
    });
    $('.dateFormat').datetimepicker({
        format: "Y-m-d",
        timepicker: true,
        yearStart: 2000,
        yearEnd: 2050,
        todayButton: false
    });
    //不显示时分秒
    $('.dateTimeFormatDay').datetimepicker({
        format: "Y-m-d",
        timepicker: false,
        yearStart: 2000,
        yearEnd: 2050,
        todayButton: true,
        scrollMonth: false,
        scrollTime: false,
        scrollInput: false,
    });
    $('.timeFormat').datetimepicker({
        format: "H:i:s",
        timepicker: true,
        yearStart: 2000,
        yearEnd: 2050,
        todayButton: false
    });
    var attr = $("a:contains('返回')").attr("href");
    if (attr) {
        if (attr.indexOf("?") == -1) {
            attr += '?index=' + localStorage['index'];
        } else {
            attr += '&index=' + localStorage['index'];
        }
        $("a:contains('返回')").attr("href", attr);
    }

    $('.select2').select2();

    $(".featureValidate").validate({
        rules: {
            sort: {
                digits: true
            },
            name: {
                required: true, maxlength: 50
            },
            title: {
                required: true, maxlength: 50
            }
        },
        errorPlacement: function (error, element) {
            $(element)
                .closest("form")
                .find(element)
                .after(error);
        },
        errorElement: "span"
    });

    $(".commonValidate").validate({
        rules: {
            sort: {
                digits: true
            },
            name: {
                required: true, maxlength: 50
            },
            title: {
                required: true, maxlength: 50
            },
            'city.id': {
                required: true
            },
            'country.id': {
                required: true
            },
            cmsusername: {
                required: true
            }
        },
        errorPlacement: function (error, element) {
            $(element)
                .closest("form")
                .find(element)
                .after(error);
        },
        errorElement: "span"
    });


    $(".itemValidate").validate({
        rules: {
            sort: {
                digits: true
            },
            name: {
                required: true, maxlength: 50
            },
            title: {
                required: true, maxlength: 50
            },
            'city.id': {
                required: true
            },
            'country.id': {
                required: true
            },
            cmsusername: {
                required: true
            },
            weight: {
                required: true, number: true
            }
        },
        errorPlacement: function (error, element) {
            $(element)
                .closest("form")
                .find(element)
                .after(error);
        },
        errorElement: "span"
    });


    $(".pushDetailValidate").validate({
        rules: {
            sort: {
                digits: true
            },
            title: {
                maxlength: 50
            },
            source: {
                required: true, maxlength: 20
            }
        },
        errorPlacement: function (error, element) {
            $(element)
                .closest("form")
                .find(element)
                .after(error);
        },
        errorElement: "span"
    });

    var listComplete = [];
    $(".langsAutoComplete").on("keyup", function (e) {
        var keyWord = $(this).val();
        if (!keyWord) {
            return;
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
                    var currEle = e.currentTarget;
                    var list = result.data;
                    if (list.length == 0) {
                        $(currEle).next().val("");
                        return;
                    }
                    for (var i = 0; i < list.length; i++) {
                        var o = list[i];
                        var complate = {};
                        complate.labelDisplay = o.message;
                        complate.label = o.message + "(" + o.keyCode + ")";
                        complate.value = o.keyCode;
                        listComplete.push(complate);
                    }
                } else {
                    alert(result.msg);
                }
            }
        });
    }).autocomplete({
        source: listComplete,
        focus: function (event, ui) {
            $(this).val(ui.item.label);
            $(this).next().val(ui.item.value);
            return false;
        },
        select: function (event, ui) {
            $(this).val(ui.item.label);
            $(this).next().val(ui.item.value);
            return false;
        }
    })

    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    String.prototype.replaceAll = function (FindText, RepText) {
        regExp = new RegExp(FindText, "g");
        return this.replace(regExp, RepText);
    }
}