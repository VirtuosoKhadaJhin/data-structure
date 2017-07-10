/**
 * Created by mylon on 2017/6/28.
 */
$(function () {

    //界面加载时，选中checked-box
    var gloable_cookieValues = getCookieValue("checked-mission-tasks");
    if (gloable_cookieValues.length != 0) {
        for (var m in gloable_cookieValues) {
            var cookie_taskId = gloable_cookieValues[m];
            var checkboxInfo = $(".input-checkbox[task-id=" + cookie_taskId + "]");
            if (checkboxInfo != undefined && checkboxInfo != null) {
                checkboxInfo.prop("checked", false);
            }
        }
    }

    // 时间查询条件显示与隐藏
    $(".task-status").on("change", function () {
        taskStatus = $(".task-status").val();
        changeTimeInputs(taskStatus);
    });

    //checkbox选中/取消事件
    $(".tbody-list .input-checkbox").on("change", function () {
        var taskId = $(this).attr("task-id"), isChecked = $(this)[0].checked;
        var cookieValues = getCookieValue("checked-mission-tasks");
        if (cookieValues.length == 0 && isChecked) {
            setSessionCookie("checked-mission-tasks", taskId, "1");
            return false;
        }
        var taskIds = cookieValues.split(",");
        if (isChecked) {
            taskIds.push(taskId);
            setSessionCookie("checked-mission-tasks", taskIds, "1");
            $(".has-choosed-task").val(taskIds.length);
        } else {
            var newTaskIds = $.makeArray(null);
            for (var i = 0; i <= taskIds.length; i++) {
                var id = taskIds[i];
                if (id != taskId) {
                    newTaskIds.push(id);
                }
            }
            setSessionCookie("checked-mission-tasks", newTaskIds, "1");
            $(".has-choosed-task").val(newTaskIds.length);
        }
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

    // 全选，全不选
    $(".th-checked").on("click", function () {
        $(".tbody-list :checkbox").click();
    });

    // 任务去掉勾选，联动去掉全选
    $(".td-checked").on("click", function () {
        if ($(this)[0].checked) {
            var checked_count = $(".tbody-list .input-checkbox:checked").length;
            var allLength = $(".tbody-list .input-checkbox").length;
            if (checked_count == allLength) {
                $(".th-checked").prop("checked", true);
            }
        } else {
            $(".th-checked").prop("checked", false);
        }
    });

    // BD单选
    $(".bd-checkd").on("click", function () {
        $(".bd-checkd-prev :checkbox").removeAttr("checked");
        $(this).prop("checked", true);
        bdId = $(this).attr("bd-id");
    });

    // 指派任务弹框
    $(".task-distribute").on("click", function () {
        $(".datetime-distrDt").val(new Date().Format("yyyy-MM-dd"));
        if (taskIds.length == 0) {
            $(".distributeResult-text").html("<strong style='color: red'>请选择任务！</strong>");
            $(".taskDistributeResultModel").modal('show');
        } else {
            $(".taskDistributeModel").modal('show');
        }
        $(".bd-checkd-prev :checkbox").removeAttr("checked");

        var cookieValues = getCookieValue("checked-mission-tasks");
        $(".task-distribute-count").val(cookieValues.length);
    });

    // 确认指派任务
    $(".sure-distribute").on("click", function () {
        var taskIds = getCookieValue("checked-mission-tasks");
        var distrDt = $(".datetime-distrDt").val();

        $(".taskDistributeModel").modal('hide');
        $(".distributeResult-text").text("任务指派成功！");

        // 如果没有选择任务
        if (taskIds.length == 0) {
            $(".distributeResult-text").html("<strong style='color: red'>请选择任务！</strong>");
            return false;
        } else if (typeof(bdId) == "undefined") {
            $(".distributeResult-text").html("<strong style='color: red'>请选择BD！</strong>");
            $(".taskDistributeResultModel").modal('show');
            return false;
        }

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
        if (taskStatus == "" || taskStatus == "APPROVED" || taskStatus == "NON_APPROVAL") {
            $(".task-distrdt,.task-auditdt,.task-finishdt").show();
        } else if (taskStatus == "UN_DISTRIBUTE") {
            $(".task-distrdt,.task-auditdt,.task-finishdt").hide();
        } else if (taskStatus == "UN_FINISH") {
            $(".task-distrdt").show();
            $(".task-auditdt,.task-finishdt").hide();
        } else if (taskStatus == "FINISHED") {
            $(".task-distrdt,.task-finishdt").show();
            $(".task-auditdt").hide();
        }
    }
});
