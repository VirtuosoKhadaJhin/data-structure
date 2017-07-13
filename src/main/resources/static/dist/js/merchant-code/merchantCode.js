/**
 * Created by mylon on 2017/7/13.
 */
$(function () {
    // 查看二维码
    $(".view-collection-code").on("click", function () {
        showCollectionCode($(this).parents("tr").attr("data-code"));
        $(".code-tip-modal").modal("show");
    });

    // 绑定
    $('.bind-number').on("click", function () {
        var collectionCode = $(this).parents("tr").attr("data-code");
        $(".modal-collection-code").text("编码：" + collectionCode);
        $(".bind-merchant-modal").modal("show");
    });

    // 解绑
    $('.unbind-number').on("click", function () {
        var code = $(this).parents("tr").attr("data-code"), mchId = $(this).parents("tr").attr("data-mchId"), codeId = $(this).parents("tr").attr("data-id");
        showCollectionCode(code);
        $(".unbind-merchant-modal .unbind-code").val(code);
        $(".unbind-merchant-modal .unbind-code-id").val(codeId);
        $(".unbind-merchant-modal .unbind-mch-id").val(mchId);
        $(".unbind-merchant-modal").modal("show");

    });

    // 确认解绑
    $(".sure-unbind").on("click", function () {
        var code = $(".unbind-merchant-modal .unbind-code").val();
        var mchId = $(".unbind-merchant-modal .unbind-mch-id").val();
        var codeId = $(".unbind-merchant-modal .unbind-code-id").val();
        $.ajax({
            url: '/merchant/unbind/number',
            data: {mchId: mchId, collectionCode: code},
            type: 'post',
            success: function (result) {
                $(".unbind-merchant-modal").modal("hide");
                if (result.code == 0) {
                    showNormalTipModal("解绑成功");
                    $(".td-merchant-mch" + codeId).html("");
                    $(".td-op-mch" + codeId + " .bind-number").show();
                    $(".td-op-mch" + codeId + " .unbind-number").hide();
                    $(".td-status-mch" + codeId + " label").text("未绑定");
                } else {
                    showNormalTipModal("解绑失败," + result.msg);
                    $(".td-status-mch" + codeId + " label").text("已绑定");
                }
            }
        });
    });

    // 搜索框重置
    $(".search-reset").on("click", function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });

    // 时间标准
    $('.searchTimeFormatDay').datetimepicker({
        format: "Y-m-d",
        timepicker: false,
        yearStart: 2000,
        yearEnd: 2050,
        todayButton: true,
        scrollMonth: false,
        scrollTime: false,
        scrollInput: false,
    });

    // 显示操作编码
    function showCollectionCode(code) {
        $(".modal-collection-code").text("编码：" + code);
    }

    // 通用结果提示框
    function showNormalTipModal(tip) {
        $(".normal-tip-modal .modal-body-content").text(tip);
        $(".normal-tip-modal").modal("show");
    }

});