/**
 * Created by Byron on 2017/7/27.
 */
$(function () {
    //是否全选
    if($("#checkboxAllStatus").val() == "true"){
        $('#checkbox-all').prop("checked",true);
    }else{
        $('#checkbox-all').prop("checked",false);
    }

    //全选按钮
    $('#checkbox-all').click(function(){
        if($(this).is(':checked')){
            $("#checkboxAllStatus").val(true);//全选状态
            $("#currentCountShow").text(parseInt($("#pageCountAll").val()));//当前勾选数据
            $('.input-checkbox').each(function(){
                $(this).prop("checked",true);
            });
        }else{
            $("#currentCountShow").text(0);
            $("#checkboxAllStatus").val(false);
            $('.input-checkbox').each(function(){
                $(this).removeAttr("checked",false);
            });
        }
    });

    //单选按钮
    $('.input-checkbox').click(function(){
        if($(this).is(':checked')){
            $("#currentCountShow").text(parseInt($("#currentCountShow").text())+1);
        }else{
            $("#currentCountShow").text(parseInt($("#currentCountShow").text())-1);
        }
    });


    //导出按钮
    $("#exportComment").on("click", function () {
        var currentCountShow = $("#currentCountShow").text();
        var ocnfirmInfo = "是否全部导出";
        if(currentCountShow != 0){
            ocnfirmInfo = "当前勾选评论"+currentCountShow+"条，导出请点击确认按钮";
        }
        window.wxc.xcConfirm(ocnfirmInfo, window.wxc.xcConfirm.typeEnum.confirm, {
            onOk: function () {
                var IdStrChecked = new Array();
                var IdStrNoChecked = new Array();
                $(" tbody tr").each(function(){
                    if($(this).find("td:first .input-checkbox").is(':checked')){
                        IdStrChecked.push($(this).data("commentid"));
                    }else{
                        IdStrNoChecked.push($(this).data("commentid"));
                    }
                });
                var map = {};//返回对象
                map["IdStrChecked"] = IdStrChecked;//未选中数据Id
                map["IdStrNoChecked"] = IdStrNoChecked;//未选中数据Id
                map["checkboxAllStatus"] = $("#checkboxAllStatus").val();//勾选状态
                var jsonMap = JSON.stringify(map);
                $.ajax({
                    url: 'exportData',
                    data: jsonMap,
                    contentType: 'application/json',
                    type: 'post',
                    success: function (result) {
                        if (result.code == 0) {
                            $("#paramFrom").attr("action", "exportComment");
                            $("#paramFrom").submit();
                            $("#paramFrom").attr("action", "");
                        }
                    }
                });
            }
        });
    })

    //清空按钮
    $("#clearCheck").on("click", function () {
        $.ajax({
            url: 'clearCheck',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            async: false,
            success: function (result) {
                if(result.code == 0){
                    $('#checkbox-all').prop("checked",false);//全选按钮
                    $("#checkboxAllStatus").val(false);//当前全选状态
                    $("#currentCountShow").text(0);//当前勾选数据
                    $('.input-checkbox').each(function(){
                        $(this).removeAttr("checked",false);
                    });
                }
            }
        });
    });

    //跳转页面 下一页
    $(".pagination li a").on("click", function () {
        var targetPage = $(this).text();//跳转目标页
        var a_href = $(this).attr("href");
        if (a_href === undefined || a_href === "" || a_href == "javascript:void(0);") {
            return false;
        }
        var map = {};//返回对象
        var tempMap = {};
        var IdStrChecked = new Array();
        var IdStrNoChecked = new Array();
        $(" tbody tr").each(function(){
            if($(this).find("td:first .input-checkbox").is(':checked')){
                tempMap[$(this).data("commentid")] =  "checked";
                IdStrChecked.push($(this).data("commentid"));
            }else{
                IdStrNoChecked.push($(this).data("commentid"));
                tempMap[$(this).data("commentid")] =  "";
            }
        });
        map["currentPage"] = $("#currentPage").val();//记录当前页数
        map["IdStrChecked"] = IdStrChecked;//选中的数据Id
        map["IdStrNoChecked"] = IdStrNoChecked;//未选中数据Id
        map["currentPageMap"] = tempMap;//当前页子数据选中状态
        map["currentCount"] = $("#currentCountShow").text();//当前选中所有条数
        map["checkboxAllStatus"] = $("#checkboxAllStatus").val();//
        map["targetPage"] = targetPage;//目标页
        var jsonMap = JSON.stringify(map);
        $.ajax({
            url: 'recordedInfo',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            async: false,
            data: jsonMap,
            success: function (result) {
            }
        });
    });

    $(".detail-item").on("click", function () {
        var modal = $("#" + $(this).attr("modal-id"));
        var id = $(this).attr("data-id");
        $.ajax({
            url: "replies", type: 'get',
            data: {id: id},
            success: function (result) {
                html = "";
                for (var i in result.data) {
                    var reply = result.data[i];
                    html += '<label>' + reply.createTime + '</label>';
                    html += '<textarea class="span10" disabled>' + reply.content + '</textarea>';
                }
                $("#replies").empty().append(html);
                $(modal).modal();
            }
        });
    });

    $(".td-opt .hide-item, .td-opt .show-item").on("click", function () {
        var isShow = true, dataId = $(this).attr("data-id");
        if ($(this).hasClass("hide-item")) {
            isShow = false;
        }
        var isReq = isShow || !isShow && confirm('确实要隐藏此评论吗?');
        if (!isReq) {
            return;
        }
        $.ajax({
            url: 'showOrHideComment',
            type: 'post',
            data: {id: dataId, isShow: isShow},
            success: function (result) {
                if (result.code == 0) {
                    window.location.reload();
                } else {
                    alert(result.msg);
                }
            }
        });
    });
});
