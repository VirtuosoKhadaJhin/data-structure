/**
 * Created by mylon on 2017/6/28.
 */
$(function () {
    var bdId;
    var mchId;
    var status;

    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });

    $(".select-country").on("change", function () {
        location.href = "list?country=" + $(this).val();
    });

    $(".th-checked").on("click", function () {
        if ($(this).prop("checked")) {
            $(".tbody-list :checkbox").prop("checked", true);

        } else {
            $(".tbody-list :checkbox").prop("checked", false);
        }
    });

    $(".td-checked").on("click", function () {
        $(".th-checked").prop("checked", false);
    });

    $(".bd-checkd").on("click", function () {
        $(".bd-checkd-prev :checkbox").removeAttr("checked");
        $(this).prop("checked", true);
        bdId = $(this).attr("bd-id");
    });

    $(".task-distribute").on("click", function () {
        $(".taskDistributeModel").modal('show');
        $(".bd-checkd-prev :checkbox").removeAttr("checked");
    });

    $(".sure-distribute").on("click", function () {
        var taskIds = [];
        var tasks = $(".tbody-list :checkbox");
        for (var i = 0; i < tasks.length; i++) {
            var task = tasks[i];
            if ($(task).prop("checked") == true) {
                taskIds.push($(task).attr("task-id"));
            }
        }

        console.log("bdId:" + bdId);
        console.log("taskIds:" + taskIds.length);

        $(".taskDistributeModel").modal('hide');
        $(".distributeResult-text").text("任务指派成功！");
        // 如果没有选择任务
        if (taskIds.length == 0) {
            $(".distributeResult-text").html("<strong style='color: red'>请选择任务！</strong>");
        }
        $(".taskDistributeResultModel").modal('show');
    });

});
