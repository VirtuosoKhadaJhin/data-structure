/**
 * Created by mylon on 2017/6/28.
 */
$(function () {
    // 进入页面勾选已经勾选的,并显示出已经勾选的任务数目
    var oldCheckedTaskIds = $(".hide-checked-taskIds").val();
    if (oldCheckedTaskIds != null && oldCheckedTaskIds.length > 0) {
        var taskIDs = oldCheckedTaskIds.split(",");
        for (var i in taskIDs) {
            var taskID = taskIDs[i];
            var _checkbox = $(".input-checkbox[task-id='" + taskID + "']");
            if (_checkbox != undefined && _checkbox != null) {
                $(_checkbox).prop("checked", true);
            }
        }
        var checked_count = $(".tbody-list .input-checkbox:checked").length;
        var allLength = $(".tbody-list .input-checkbox").length;
        if (checked_count == allLength) {
            $(".th-checked").prop("checked", true);
        }
    }

    // 批量查询商户提示
    $(".batch-mch-id").focus(function () {
        $(".mch-id-tip").show("fast");
    });

    // 分页查询时携带task参数
    $(".pagination li a").on("click", function () {
        var a_href = $(this).attr("href");
        if (a_href === undefined || a_href === "" || a_href == "javascript:void(0);") {
            return false;
        }
        var hideValues = $(".hide-checked-taskIds").val();
        if (a_href.indexOf("taskIds") != -1) {
            var currUrl = window.location.search, indexNum = getUrlParamValue(a_href, "index");
            var newUrl = replaceUrlParamVal(currUrl, "index", escape(indexNum));
            newUrl = replaceUrlParamVal(newUrl, "taskIds", escape(hideValues));
            if (getUrlParamValue(newUrl, "index") == null) {
                newUrl = newUrl + "&index=" + indexNum;
            }
            $(this).attr("href", newUrl);
        } else {
            $(this).attr("href", a_href + "&taskIds=" + escape(hideValues));
        }
    });

    // th全选，全不选
    $("th .th-checked").on("change", function () {
        var newArray = $.makeArray(null);
        var isChecked = $(this)[0].checked;
        $(".tbody-list .input-checkbox").each(function () {
            var taskID = $(this).attr("task-id");
            var hasChecked = $(this)[0].checked;
            if (isChecked && !hasChecked) {
                $(this).prop("checked", true);
                newArray.push(taskID);
            }
            if (!isChecked && hasChecked) {
                $(this).prop("checked", false);
                newArray.push(taskID);
            }
        });
        if (newArray.length == 0) {
            return false;
        }
        var hideValues = $(".hide-checked-taskIds").val();
        if (isChecked) {//全选
            // 防止重复
            newArray = array_remove_repeat(hideValues.split(",").concat(newArray));
            if (newArray[0] == "") {
                newArray.splice(0, 1);
            }
            $("#checkbox-all").prop("checked", true);
            $(".hide-checked-taskIds").val(newArray.toString());
            $(".has-choosed-task").html("<span class='badge badge-important'>" + newArray.length + "</span>");
            return false;
        }
        //全不选
        var checkedValues = hideValues.split(","), checkedTask = $.makeArray(null);
        for (var m in checkedValues) {
            var hasSame = false;
            for (var n in newArray) {
                if (newArray[n] != checkedValues[m]) {
                    continue;
                }
                hasSame = true;
            }
            if (!hasSame) {
                checkedTask.push(checkedValues[m]);
            }
        }
        if (checkedTask.length == 0) {
            $(".hide-checked-taskIds").val("");
        } else {
            $(".hide-checked-taskIds").val(checkedTask.toString());
        }
        $("#checkbox-all").prop("checked", false);
        $(".has-choosed-task").html("<span class='badge badge-important'>" + checkedTask.length + "</span>");
    });

    // 检查td的选中与取消选中
    $(".td-checked").on("click", function () {
        var checkedValues = $(".hide-checked-taskIds").val().split(",");
        if ($(this)[0].checked) {
            var checked_count = $(".tbody-list .input-checkbox:checked").length;
            var allLength = $(".tbody-list .input-checkbox").length;
            if (checked_count == allLength) {
                $(".th-checked").prop("checked", true);
            }
            // add into hideValues
        } else {
            $(".th-checked").prop("checked", false);
            // remove from hideValues
        }
        var hasSame = false;
        var taskId = $(this).attr("task-id");
        for (var m in checkedValues) {
            if (checkedValues[m] == taskId) {
                hasSame = true;
                checkedValues.splice(checkedValues.indexOf(taskId), 1);
            }
        }
        if (!hasSame) {
            checkedValues.push(taskId);
        }
        if (checkedValues[0] == "") {
            checkedValues.splice(0, 1);
        }
        $(".has-choosed-task").html("<span class='badge badge-important'>" + checkedValues.length + "</span>");
        $(".hide-checked-taskIds").val(checkedValues.toString());
    });

    // 时间查询条件显示与隐藏
    $(".task-status").on("change", function () {
        changeTimeInputs($(".task-status").val());
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

    // BD单选
    $(".bd-checkbox-input").on("click", function () {
        $(".bd-checkd-prev :checkbox").removeAttr("checked");
        $(this).prop("checked", true);
    });

    // 指派任务弹框
    $(".task-distribute").on("click", function () {
        $(".datetime-distrDt").val(new Date().Format("yyyy-MM-dd"));
        var hideValues = $(".hide-checked-taskIds").val();
        var checkedValues = hideValues.split(",")
        if (hideValues == null || hideValues == "") {
            $(".distributeResult-text").html("<strong style='color: red'>请选择任务！</strong>");
            $(".taskDistributeResultModel").modal('show');
        } else {
            $(".taskDistributeModel").modal('show');
        }
        $(".task-distribute-count").text(checkedValues.length);
        $(".bd-checkbox-input").removeAttr("checked");
    });

    //弹窗初始化数据
    $(".taskDistributeModel").on("show.bs.modal", function () {
        var taskIds = $(".hide-checked-taskIds").val();
        if (taskIds == null || taskIds.length == 0) {
            $(".task-distribute-count").val(0);
        } else {
            $(".task-distribute-count").val(taskIds.toString());
        }
    });

    // 确认指派任务
    $(".sure-distribute").on("click", function () {
        var taskIds = $(".hide-checked-taskIds").val().split(",");
        var distrDt = $(".datetime-distrDt").val();
        var bdId = $("input[name='bdCheckboxInput']:checked").attr("bd-id");

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

    // 指派完成重新加载页面
    $(".taskDistributeResultModel").on("hide.bs.modal", function () {
        var result = $(".audit-result").val();
        if (result == 0 && result != "") {
            var currUrl = window.location.search;
            var newUrl = replaceUrlParamVal(currUrl, "taskIds", "");
            window.location = newUrl;
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

    // 指派成功刷新页面
    $(".publicSwichResultModel").on("hide.bs.modal", function () {
        var result = $(".isPublic-result").val();
        if (result == 0 && result != "") {
            window.location.reload();
        }
    });

    // 可见不可见
    $(".publicSwichModel .btn-colse").click(function () {
        var oldPublic = $(".public-default-public").val();
        $(".radio-public[value=" + oldPublic + "]").prop("checked", true);
    });

    // 可见不可见
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
        $(".task-distrdt input,.task-auditdt input,.task-finishdt input").val("");
        if (taskStatus == "") {
            $(".task-distrdt,.task-auditdt,.task-finishdt").show();
        } else if (taskStatus == "APPROVED" || taskStatus == "NON_APPROVAL") {
            $(".task-auditdt").show(), $(".task-distrdt,.task-finishdt").hide();
        } else if (taskStatus == "UN_DISTRIBUTE") {
            $(".task-distrdt,.task-auditdt,.task-finishdt").hide();
        } else if (taskStatus == "UN_FINISH") {
            $(".task-distrdt").show(), $(".task-auditdt,.task-finishdt").hide();
        } else if (taskStatus == "FINISHED") {
            $(".task-finishdt,.task-distrdt").show(), $(".task-auditdt").hide();
        }
    }

    // 去处重复
    function array_remove_repeat(a) {
        var r = [];
        for (var i = 0; i < a.length; i++) {
            var flag = true;
            var temp = a[i];
            for (var j = 0; j < r.length; j++) {
                if (temp === r[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                r.push(temp);
            }
        }
        return r;
    }

})
;
