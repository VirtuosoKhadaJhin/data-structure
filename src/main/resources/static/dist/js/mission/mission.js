/**
 * Created by mylon on 2017/6/28.
 */
$(function () {
    var mchId;
    var status;

    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });

    $(".select-country").on("change", function () {
        location.href = "list?country=" + $(this).val();
    });

    $(".approval").on("click", function () {
        $(".approval-status").val("APPROVED");
        $(".remark-text").val('');
        $(".approvalModel").modal('show');
        mchId = $(this).parent().attr("data-mchId");
    });

    $(".approval-status").on("change", function () {
        if ("NON_APPROVAL" == $(this).val()) {
            $(".remark-info").show();
        } else {
            $(".remark-info").hide();
        }
    });

    $('.approvalModel').on('shown.bs.modal', function () {
        $(".remark-info").hide();
    })

    $(".sure-approval").on("click", function () {
        var remark = $(".remark-text").val();

        status = $(".approval-status").val();
        if (status == "APPROVED") {
            remark = "";
        }
        var data = {
            mchId: mchId,
            status: status,
            remark: remark,
        };
        console.log(data);
        $.ajax({
            url: 'approval',
            data: JSON.stringify(data),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    $(".approvalModel").modal('hide');
                    $(".approvalResult-text").text("审批成功！");
                    $("#data-" + mchId).remove();
                    $(".approvalResultModel").modal('show');
                } else {
                    $(".approvalModel").modal('hide');
                    $(".approvalResult-text").text(result.msg);
                    $(".approvalResultModel").modal('show');
                }
            }
        });

    });

});
