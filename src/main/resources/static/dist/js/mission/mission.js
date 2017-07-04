/**
 * Created by mylon on 2017/6/28.
 */
$(function () {

    // 改变任务状态
    $(".task-status").on("change", function () {
        if ($(this).val() != "FINISHED") {
            $(".task-status-time").show();
        } else {
            $("#date-sh-date").prop("value", "");
            $(".task-status-time").hide();
        }
    });

    // 搜索框重置
    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
        // $(".select-country").val("1");
    });

    // 审批弹窗
    $(".approval").on("click", function () {
        $(".approval-status").val("APPROVED");
        $(".remark-text").val('');
        $(".approvalModel").modal('show');
        $(".modal .hide-mchId").val($(this).parents("tr").attr("data-mchId"));
    });

    var status = $(".task-status").val();
    if ("NON_APPROVAL" == status || "APPROVED" == status) {
        $("#sh-date").show();
    } else {
        $("#date-sh-date").prop("value", "");
        $("#sh-date").hide();
    }

    // 改变备注的显示与隐藏
    $(".approval-status").on("change", function () {
        if ("NON_APPROVAL" == $(this).val() || "APPROVED" == $(this).val()) {
            $(".remark-info").show();
        } else {
            $(".remark-info").hide();
        }
    });

    // 审批弹窗事件
    $('.approvalModel').on('shown.bs.modal', function () {
        $(".remark-info").hide();
    });

    $(".approvalResultModel").on("hide.bs.modal", function () {
        var result = $(".audit-result").val();
        if (result == 0 && result != "") {
            window.location.reload();
        }
    });

    $(".approvalModel").on("hide.bs.modal", function () {
        $(".remark-info").hide();
    });

    // 确认审批
    $(".sure-approval").on("click", function () {
        var remark = $(".remark-text").val();

        var status = $(".approval-status").val();
        var data, mchId = $(".modal .hide-mchId").val();
        if (status == "APPROVED") {
            data = {
                mchId: mchId,
                status: status
            };
        } else {
            data = {
                mchId: mchId,
                status: status,
                remark: remark,
            };
        }
        $.ajax({
            url: 'approval',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    $(".approvalResult-text").text("审批成功！");
                } else {
                    $(".approvalResult-text").text(result.msg);
                }
                $(".approvalModel").modal('hide');
                $(".approvalResultModel").modal('show');
                $(".approvalResultModel .audit-result").val(result.code);
            }
        });
    });

});
