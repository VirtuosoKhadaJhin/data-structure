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
        $(".approvalModel").modal('show');
        mchId = $(this).parent().attr("data-mchId");
        status = $(".approval-status").val();
    });

    $('.approval').on('show.bs.modal', function () {
        mchId = "";
        status = "";
        $(".remark-text").val("");
    })

    $(".sure-approval").on("click", function () {
        var remark = $(".remark-text").val();

        var data = {
            mchId: mchId,
            status: status,
            remark: remark,
        };
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
                    $(".approvalResultModel").modal('show');
                }else{
                    $(".approvalModel").modal('hide');
                    $(".approvalResult-text").text(result.msg);
                    $(".approvalResultModel").modal('show');
                }
            }
        });

    });

});
