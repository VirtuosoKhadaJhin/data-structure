/**
 * Created by mylon on 2017/7/13.
 */
$(function () {
    $(".has-not-bind-count-label").text($(".has-not-bind-count").val());
    $(".all-code-count-label").text($(".all-code-count").val());

    // 查看二维码
    $(".view-collection-code").on("click", function () {
        showCollectionCode($(this).parents("tr").attr("data-code"))
        var codeImg = $(this).parents("tr").attr("data-code-img");
        if (codeImg == undefined || codeImg == "" || codeImg == null) {
            showNormalTipModal("暂无收款码");
            return false;
        }
        $(".code-tip-modal .modal-collection-code-img").attr("src", codeImg);
        $(".code-tip-modal").modal("show");
    });

    // 绑定
    $('.bind-number').on("click", function () {
        var collectionCode = $(this).parents("tr").attr("data-code");
        $(".modal-collection-code").text("编码：" + collectionCode);
        var code = $(this).parents("tr").attr("data-code");
        $(".bind-merchant-modal .bind-code-number").val(code);
        $(".bind-merchant-modal").modal("show");
    });

    // 解绑
    $('.unbind-number').on("click", function () {
        var code = $(this).parents("tr").attr("data-code"), mchId = $(this).parents("tr").attr("data-mchId"), codeId = $(this).parents("tr").attr("data-id");
        showCollectionCode(code);
        $(".unbind-merchant-modal .unbind-code").val(code), $(".unbind-merchant-modal .unbind-code-id").val(codeId), $(".unbind-merchant-modal .unbind-mch-id").val(mchId);
        $(".unbind-merchant-modal").modal("show");

    });

    // 确认解绑
    $(".sure-unbind").on("click", function () {
        var code = $(".unbind-merchant-modal .unbind-code").val(), mchId = $(".unbind-merchant-modal .unbind-mch-id").val(), codeId = $(".unbind-merchant-modal .unbind-code-id").val();
        $.ajax({
            url: '/merchant/unbind/number',
            data: {mchId: mchId, collectionCode: code},
            type: 'post',
            success: function (result) {
                $(".unbind-merchant-modal").modal("hide");
                if (result.code == 0) {
                    showNormalTipModal("解绑成功");
                    $(".td-merchant-mch" + codeId).html("");
                    $(".td-url-mch" + codeId).html("");
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

    // 查询提交表单前检验批量商户id是否合法
    $(".search-form .btn-ok").on("click", function () {
        var mchIdStr = $(".batch-merchant-ids").val(), codeStr = $(".batch-collection-codes").val();

        var patten = new RegExp(/^[\d,]+$/);
        if (mchIdStr != undefined && mchIdStr != "" && mchIdStr.length > 0) {
            var regResult = patten.test(mchIdStr);
            if (!regResult || mchIdStr.split(",").length > 20) {
                showNormalTipModal("商户id须以英文逗号','或换行分隔,最多支持20个商户ID。");
                return false;
            }
            $(".batch-merchant-ids").val(mchIdStr);
        }
        if (codeStr != undefined && codeStr != "" && codeStr.length > 0) {
            var regResult = patten.test(codeStr);
            if (!regResult || codeStr.split(",").length > 20) {
                showNormalTipModal("编码须以英文逗号','或换行分隔,最多支持20个商户ID。");
                return false;
            }
            $(".batch-collection-codes").val(codeStr);
        }

        submitForm();
    });


    // 搜索框重置
    $(".search-reset").on("click", function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
        submitForm();
    });

    // 时间标准
    $('.searchTimeFormatDay').datetimepicker({
        format: "Y/m/d",
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

    // url提交表单
    function submitForm() {
        var codes = $(".batch-collection-codes").val(),
            countryId = $(".search-country-select").val(),
            mchIds = $(".batch-merchant-ids").val(),
            mchName = $(".search-merchant-name-input").val(),
            status = $(".search-status-select").val(),
            startDate = $(".search-start-time-input").val(),
            endDate = $(".search-end-time-input").val();

        var searchUrl = "collectioncodes?";

        if (codes != null && codes != "") {
            searchUrl += "codes=" + codes;
        }
        if (countryId != null && countryId != "") {
            searchUrl += "&countryId=" + countryId;
        }
        if (mchIds != null && mchIds != "") {
            searchUrl += "&mchIds=" + mchIds;
        }
        if (mchName != null && mchName != "") {
            searchUrl += "&mchName=" + mchName;
        }
        if (status != null && status != "") {
            searchUrl += "&status=" + status;
        }
        if (startDate != null && startDate != "") {
            searchUrl += "&startDate=" + startDate;
        }
        if (endDate != null && endDate != "") {
            searchUrl += "&endDate=" + endDate;
        }
        location.href = searchUrl;

    }

});