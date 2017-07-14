/**
 * Created by mylon on 2017/7/13.
 */
$(function () {
    $(".all-code-count-label").text($(".all-code-count").val());

    // 查看二维码
    $(".view-collection-code").on("click", function () {
        showCollectionCode($(this).parents("tr").attr("data-code"))
        var codeImg = $(this).parents("tr").attr("data-code-img");
        var codeId = $(this).parents("tr").attr("data-id");
        var codeImgExists = $(".td-url-mch" + codeId + " .td-code-url").attr("href");
        if (codeImgExists == undefined || codeImgExists == "" || codeImgExists == null) {
            showNormalTipModal("暂无收款码");
            return false;
        }
        $('.qrcode').empty();
        $('.qrcode').qrcode({width: 200, height: 200, correctLevel: 0, text: codeImg});
        $(".code-tip-modal").modal("show");
    });

    // 绑定
    $('.bind-number').on("click", function () {
        var countryName = $(this).parents("tr").attr("data-country-name"), countryId = $(this).parents("tr").attr("data-country-id");
        $(".bind-merchant-modal .bind-country input").val(countryName).attr("data-country-id", countryId);
        $(".bind-merchant-modal .sure-bind").prop("disabled", "disabled");
        $(".bind-merchant-modal .bind-merchant-name select").html("<option>加载中</option>");
        $(".bind-merchant-modal .bind-merchant-name select").prop("disabled", "disabled");
        $.ajax({
            url: '/merchant/' + countryId + '/merchantlist',
            type: 'get',
            success: function (result) {
                $(".bind-merchant-modal .bind-merchant-name select").html("");
                $(".bind-merchant-modal .sure-bind").prop("disabled", "");
                $(".bind-merchant-modal .bind-merchant-name select").prop("disabled", "");
                if (result.code == 0) {
                    var merchants = result.data;
                    $.each(merchants.content, function (mchKey, mchVal) {
                        $(".bind-merchant-modal .bind-merchant-name select").append("<option value='" + mchVal.id + "'>" + mchVal.name + "</option>");
                    });
                } else {
                    showBindResultModal("查询商户信息失败!" + result.msg);
                    return false;
                }
            }
        });

        $(".modal-collection-code").text("编码：" + $(this).parents("tr").attr("data-code"));
        $(".bind-merchant-modal .bind-code-number").val($(this).parents("tr").attr("data-code"));
        $(".bind-url-tip, .bind-merchant-name-tip").removeClass("show-tip");
        $(".bind-merchant-modal").modal("show");
    });

    // 确认绑定
    $(".bind-merchant-modal .sure-bind").on("click", function () {
        var code = $(".bind-merchant-modal .bind-code-number").val(), mchId = $(".bind-merchant-name select").val();
        if (mchId == undefined || mchId == "" || mchId == null) {
            $(".bind-merchant-name-tip").addClass("show-tip");
            return false;
        }
        $.ajax({
            url: '/merchant/bind/number',
            data: {mchId: mchId, collectionCode: code},
            type: 'post',
            success: function (result) {
                $(".bind-code-result").val(result.code);
                $(".bind-merchant-modal").modal("hide");
                if (result.code == 0) {
                    showBindResultModal("绑定成功!");
                } else {
                    showBindResultModal("绑定失败!" + result.msg);
                }
            }
        });

        return true;
    });

    // 绑定成功刷新页面
    $(".bind-merchant-result-modal").on("hide.bs.modal", function () {
        var result = $(".bind-code-result").val();
        if (result == 0 && result != "") {
            window.location.reload();
        }
    });


    // 解绑
    $('.unbind-number').on("click", function () {
        var code = $(this).parents("tr").attr("data-code"), mchId = $(this).parents("tr").attr("data-mchId"), codeId = $(this).parents("tr").attr("data-id");
        $(".unbind-merchant-modal .unbind-code").val(code), $(".unbind-merchant-modal .unbind-code-id").val(codeId), $(".unbind-merchant-modal .unbind-mch-id").val(mchId);
        showCollectionCode(code);
        $(".unbind-merchant-modal").modal("show");

    });

    // 确认解绑
    $(".unbind-merchant-modal .sure-unbind").on("click", function () {
        var code = $(".unbind-merchant-modal .unbind-code").val(), mchId = $(".unbind-merchant-modal .unbind-mch-id").val(), codeId = $(".unbind-merchant-modal .unbind-code-id").val();
        $.ajax({
            url: '/merchant/unbind/number',
            data: {mchId: mchId, collectionCode: code},
            type: 'post',
            success: function (result) {
                $(".unbind-merchant-modal").modal("hide");
                if (result.code == 0) {
                    showNormalTipModal("解绑成功");
                    $(".td-url-mch" + codeId).html("");
                    $(".td-merchant-mch" + codeId).html("");
                    $(".td-op-mch" + codeId + " .bind-number").show();
                    $(".td-op-mch" + codeId + " .unbind-number").hide();
                    $(".td-status-mch" + codeId + " label").text("未绑定");
                    $(".td-update-time" + codeId + " .td-update-modifier").text(result.data.modifier);
                    $(".td-update-time" + codeId + " .td-update-time-label").text(result.data.updateTime);
                } else {
                    showNormalTipModal("解绑失败," + result.msg);
                    $(".td-status-mch" + codeId + " label").text("已绑定");
                }
            }
        });
    });

    // 查询提交表单前检验批量商户id是否合法
    $(".search-form .btn-ok").on("click", function () {
        $.makeArray()
        var mchIdStr = $(".batch-merchant-ids").val(), codeStr = $(".batch-collection-codes").val();

        var patten = new RegExp(/^[\d,]+$/);
        if (codeStr != undefined && codeStr != "" && codeStr.length > 0) {
            codeStr = codeStr.replaceAll(/(\r\n|\n|\r)/gm, ',').replaceAll(",,", ",").replaceAll(" ", "");
            if (codeStr.startsWith(",")) {
                codeStr = codeStr.substring(1, codeStr.length);
            }
            if (codeStr.endsWith(",")) {
                codeStr = codeStr.substring(0, codeStr.length - 1);
            }
            var regResult = patten.test(codeStr);

            if (!regResult || codeStr.split(",").length > 20) {
                showNormalTipModal("编码须以英文逗号','或换行分隔,最多支持20个编码。");
                return false;
            }
            $(".batch-collection-codes").val(codeStr);
        }
        if (mchIdStr != undefined && mchIdStr != "" && mchIdStr.length > 0) {
            mchIdStr = mchIdStr.replaceAll(/(\r\n|\n|\r)/gm, ',').replaceAll(",,", ",").replaceAll(" ", "");
            if (mchIdStr.startsWith(",")) {
                mchIdStr = mchIdStr.substring(1, mchIdStr.length);
            }
            if (mchIdStr.endsWith(",")) {
                mchIdStr = mchIdStr.substring(0, mchIdStr.length - 1);
            }
            var regResult = patten.test(mchIdStr);
            if (!regResult || mchIdStr.split(",").length > 20) {
                showNormalTipModal("商户id须以英文逗号','或换行分隔,最多支持20个商户ID。");
                return false;
            }
            $(".batch-merchant-ids").val(mchIdStr);
        }

        submitForm();
    });


    // 搜索框重置
    $(".search-reset").on("click", function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
        submitForm();
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

    // 显示绑定结果 刷新页面
    function showBindResultModal(tip) {
        $(".bind-merchant-result-modal .modal-body-content").text(tip);
        $(".bind-merchant-result-modal").modal("show");
    }

    // url提交表单
    function submitForm() {
        var urlParamObj = {
            codes: $(".batch-collection-codes").val(),
            countryId: $(".search-country-select").val(),
            mchIds: $(".batch-merchant-ids").val(),
            mchName: $(".search-merchant-name-input").val(),
            status: $(".search-status-select").val(),
            startDate: $(".search-start-time-input").val(),
            endDate: $(".search-end-time-input").val()
        };

        var searchUrl = "collectioncodes?";

        $.each(urlParamObj, function (paramKey, paramVal) {
            if (paramVal != null && paramVal != "") {
                if (searchUrl == "collectioncodes?")
                    searchUrl += paramKey + "=" + paramVal;
                else
                    searchUrl += "&" + paramKey + "=" + paramVal;
            }
        });

        location.href = searchUrl;
    }

    // 时间标准
    $('.searchTimeFormatDay').datetimepicker({
        format: "Y-m-d H:i:s",
        timepicker: true,
        yearStart: 2000,
        yearEnd: 2050,
        todayButton: true,
        scrollMonth: false,
        scrollTime: false,
        scrollInput: false,
    });

    String.prototype.replaceAll = function (FindText, RepText) {
        regExp = new RegExp(FindText, "g");
        return this.replace(regExp, RepText);
    }

});