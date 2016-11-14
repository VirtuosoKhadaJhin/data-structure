var defaultOption = null;
$("select[target-name]").each(function () {
    $(this).on("change", function () {
        var target = $(this).attr("target-name");
        var url = $(this).attr("target-url");
        var value = $(this).val();

        $.ajax({
            url: url, async: false, data: {id: value},
            success: function (result) {
                var htm = '';
                $.each(result.data, function (k, v) {
                    htm += '<option value="' + v.id + '">' + v.id + ":" + v.name;
                });
                target = $("select[name='" + target + "']");
                if (defaultOption == null) {
                    defaultOption = "" + $(target).html();
                }
                htm = defaultOption + htm;
                $(target).html(htm);
            }
        });
    }).trigger("change");
    var target = $(this).attr("target-name");
    target = $("select[name='" + target + "']");
    var value = $(target).attr("def-value");
    if (value) {
        $(target).val(value);
    }
});

