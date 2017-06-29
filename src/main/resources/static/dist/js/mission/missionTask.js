/**
 * Created by mylon on 2017/6/28.
 */
$(function () {
    var bdId;
    var mchId;
    var status;

    // 可见
    $(".ispublic").on("click", function () {
        $(".publicSwichModel").modal('show');
        $(".public-swich-text").text("确定所有成员可见吗？");

    });

    // 不可见
    $(".nepublic").on("click", function () {
        $(".publicSwichModel").modal('show');
        $(".public-swich-text").text("确定所有成员不可见吗？");

    });

    // 搜索框重置
    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });

    // 改变国家
    $(".select-country").on("change", function () {
        location.href = "list?country=" + $(this).val();
    });

    // 全选，全不选
    $(".th-checked").on("click", function () {
        if ($(this).prop("checked")) {
            $(".tbody-list :checkbox").prop("checked", true);

        } else {
            $(".tbody-list :checkbox").prop("checked", false);
        }
    });

    // 任务单选
    $(".td-checked").on("click", function () {
        $(".th-checked").prop("checked", false);
    });

    // BD单选
    $(".bd-checkd").on("click", function () {
        $(".bd-checkd-prev :checkbox").removeAttr("checked");
        $(this).prop("checked", true);
        bdId = $(this).attr("bd-id");
    });

    // 指派任务弹框
    $(".task-distribute").on("click", function () {
        $(".taskDistributeModel").modal('show');
        $(".bd-checkd-prev :checkbox").removeAttr("checked");
    });

    // 确认指派任务
    $(".sure-distribute").on("click", function () {
        var taskIds = [];
        var tasks = $(".tbody-list :checkbox");
        for (var i = 0; i < tasks.length; i++) {
            var task = tasks[i];
            if ($(task).prop("checked") == true) {
                taskIds.push($(task).attr("task-id"));
            }
        }

        console.log("bdId:" + (typeof(bdId) == "undefined"));
        console.log("taskIds:" + taskIds.length);

        $(".taskDistributeModel").modal('hide');
        $(".distributeResult-text").text("任务指派成功！");

        // 如果没有选择任务
        if (taskIds.length == 0) {
            $(".distributeResult-text").html("<strong style='color: #aaff69'>请选择任务！</strong>");
        }else if (typeof(bdId) == "undefined") {
            $(".distributeResult-text").html("<strong style='color: red'>请选择BD！</strong>");
        }else{
            $(".distributeResult-text").html("任务指派成功！");
        }
        $(".taskDistributeResultModel").modal('show');
    });

});
