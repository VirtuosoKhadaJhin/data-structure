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

}