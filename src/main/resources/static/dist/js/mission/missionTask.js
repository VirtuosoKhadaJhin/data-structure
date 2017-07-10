/**
 * Created by mylon on 2017/6/28.
 */
$(function () {
    var bdId, status;

    // 绑定分页点击事件,保存选中任务
    $(".pagination ul li a").on("click", function () {
        var taskIds = getTaskIds();
        return false;
    });

    // 时间查询条件显示与隐藏
    var taskStatus = $(".task-status").val();
    changeTimeInputs(taskStatus);

    // 时间查询条件显示与隐藏
    $(".task-status").on("change", function () {
        taskStatus = $(".task-status").val();
        changeTimeInputs(taskStatus);
    });

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

    // 监测checkBox改变
    $(":checkbox").on("change", function () {
        var tasks = getTaskIds();
        $(".hasChoosedCount").text(tasks.length);
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
        var date = new Date().Format("yyyy-MM-dd");
        $(".datetime-distrDt").val(date);
        var taskIds = [];
        var tasks = $(".tbody-list :checkbox");
        for (var i = 0; i < tasks.length; i++) {
            var task = tasks[i];
            if ($(task).prop("checked") == true) {
                taskIds.push($(task).attr("task-id"));
            }
        }
        if (taskIds.length == 0) {
            $(".distributeResult-text").html("<strong style='color: red'>请选择任务！</strong>");
            $(".taskDistributeResultModel").modal('show');
        } else {
            $(".taskDistributeModel").modal('show');
        }

        $(".bd-checkd-prev :checkbox").removeAttr("checked");
    });

    // 确认指派任务
    $(".sure-distribute").on("click", function () {
        var taskIds = getTaskIds();
        var distrDt = $(".datetime-distrDt").val();


        $(".taskDistributeModel").modal('hide');
        $(".distributeResult-text").text("任务指派成功！");

        // 如果没有选择任务
        if (taskIds.length == 0) {
            $(".distributeResult-text").html("<strong style='color: red'>请选择任务！</strong>");
        } else if (typeof(bdId) == "undefined") {
            $(".distributeResult-text").html("<strong style='color: red'>请选择BD！</strong>");
            $(".taskDistributeResultModel").modal('show');
        } else {
            var data = {
                bdId: bdId,
                taskIds: taskIds,
                distrDt: distrDt
            };
            $.ajax({
                url: 'distributeTask',
                data: JSON.stringify(data),
                type: 'post',
                dataType: 'json',
                contentType: 'application/json',
                success: function (result) {
                    if (result.code == 0) {
                        $(".distributeResult-text").html("任务指派成功！");
                        $(".taskDistributeResultModel").modal('show');
                    } else {
                        $(".distributeResult-text").html("任务指派失败！");
                        $(".taskDistributeResultModel").modal('show');
                    }
                    $(".taskDistributeResultModel .audit-result").val(result.code);
                }
            });
        }
    });

    $(".taskDistributeResultModel").on("hide.bs.modal", function () {
        var result = $(".audit-result").val();
        if (result == 0 && result != "") {
            window.location.reload();
        }
    });

    // 设置可见不可见
    $(".radio-public").on("click", function () {
        var isPublic = $(this).val();
        var groupId = $(".public-default-val").val();
        $(".publicSwichModel .hide-public").val(isPublic);
        $(".publicSwichModel .hide-groupId").val(groupId);
        var msgStr = "可见";
        if (isPublic != 1) {
            msgStr = "不可见";
        }
        $(".public-switch-text").html("是否确定对所有成员 <a style = 'color:red; font-weight: bold;'>" + msgStr + "</a>！");
        $(".publicSwichModel").modal("show");
    });

    // 设置可见不可见
    $(".sure-public-switch").on("click", function () {
        var isPublic = $("#hide-isPublic").val();
        var groupId = $(".publicSwichModel .hide-groupId").val();

        if (isPublic == 0) {
            isPublic = false;
        } else {
            isPublic = true;
        }

        var data = {groupId: groupId, isPublic: isPublic};
        $.ajax({
            url: 'updateGroupPublic',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    $(".public-switch-result-text").text("操作成功");
                    window.location.reload();
                } else {
                    $(".public-switch-result-text").text(result.msg);
                }
                $(".publicSwichModel").modal('hide');
                $(".publicSwichResultModel").modal('show');
                $(".publicSwichResultModel .isPublic-result").val(result.code);
            }
        });
    });

    $(".publicSwichResultModel").on("hide.bs.modal", function () {
        var result = $(".isPublic-result").val();
        if (result == 0 && result != "") {
            window.location.reload();
        }
    });

    $(".publicSwichModel .btn-colse").click(function () {
        var oldPublic = $(".public-default-public").val();
        $(".radio-public[value=" + oldPublic + "]").prop("checked", true);

    });

    $(".publicSwichModel").on("hide.bs.modal", function () {
        var oldPublic = $(".public-default-public").val();
        $(".radio-public[value=" + oldPublic + "]").prop("checked", true);
    });

    // 获取taskIds 页面checkbox与cookie里面的值
    function getTaskIds() {
        var taskIds = [];
        var tasks = $(".tbody-list :checkbox");
        for (var i = 0; i < tasks.length; i++) {
            var task = tasks[i];
            if ($(task).prop("checked") == true) {
                taskIds.push($(task).attr("task-id"));
            }
        }
        return taskIds;
    }

    // 改变事件查询框的显示与隐藏
    function changeTimeInputs(taskStatus) {
        var distrDt = $(".task-distrdt");
        var auditDt = $(".task-auditdt");
        var finishDt = $(".task-finishdt");

        if (taskStatus == "") {
            $(distrDt).show();
            $(auditDt).show();
            $(finishDt).show();
        } else if (taskStatus == "UN_DISTRIBUTE") {
            $(distrDt).hide();
            $(auditDt).hide();
            $(finishDt).hide();
        } else if (taskStatus == "UN_FINISH") {
            $(distrDt).show();
            $(auditDt).hide();
            $(finishDt).hide();
        } else if (taskStatus == "FINISHED") {
            $(distrDt).show();
            $(auditDt).hide();
            $(finishDt).show();
        } else if (taskStatus == "APPROVED") {
            $(distrDt).show();
            $(auditDt).show();
            $(finishDt).show();
        } else if (taskStatus == "NON_APPROVAL") {
            $(distrDt).show();
            $(auditDt).show();
            $(finishDt).show();
        }

    }

});
