/**
 * Created by mylon on 2017/6/26.
 */
$(function () {
    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });

    var pcatId;
    $(".tr-class").click(function () {
        pageNum = 1;
        $("#tb-data").empty();
        var _this = $(this);
        pcatId = $(this).attr("data-id");
        var nextTr = $(this).next();
        if (nextTr.hasClass("tr-append-class")) {
            nextTr.remove();
            return false;
        }
        if ($("tbody .tr-append-class").length > 0) {
            $(".tr-append-class").remove();
        }
        var tableHTMl = subTableThead();

        var dataList = subDataList(pcatId);

        var tdHTMl = subTableTd(dataList);

        var pageHtml = subPagination(dataList);

        var noResultHtml = "";

        if (dataList.totalPages == 0) {
            noResultHtml = "<div class='no-result-div' style='text-align: center;vertical-align: middle;height: 50px;line-height: 50px;'>" +
                "<p>暂无商户分类！</p>" +
                "</div>";
        }

        var appendTableHtml = "<tr class='tr-append-class' width='90%'>" +
            "<td colspan='8' style='text-align:center;' id='tb-data'>" +
            tableHTMl +
            tdHTMl +
            noResultHtml +
            pageHtml +
            "</td>" +
            "</tr>"
        $(appendTableHtml).insertAfter($(this));
    });

    // 数据
    var subTrColors = ["sub-tr-color1", "sub-tr-color2"];

    function subTableTd(dataList) {
        var tdHtmls = "";
        var trColor = 0;
        for (var i = 0; i < dataList.content.length; i++) {
            var merchantCatVo = dataList.content[i];
            var name = "";
            var localNameLabel = "";
            var enNameLabel = "";
            var shortname = "";
            var display = "隐藏";

            if (merchantCatVo.name != null) {
                name = merchantCatVo.name;
            }
            if (merchantCatVo.localNameLabel != null) {
                localNameLabel = merchantCatVo.localNameLabel;
            }
            if (merchantCatVo.enNameLabel != null) {
                enNameLabel = merchantCatVo.enNameLabel;
            }
            if (merchantCatVo.shortname != null) {
                shortname = merchantCatVo.shortname;
            }
            if (merchantCatVo.display) {
                display = "显示";
            }

            var subTrColor = subTrColors[trColor];

            var tdHtml = "<tr class='" + subTrColor + "'>" +
                "<td class='sub-td'>" + (i + 1) + "</td>" +
                "<td class='sub-td'>" + name + "</td>" +
                "<td class='sub-td'>" + localNameLabel + "</td>" +
                "<td class='sub-td'>" + enNameLabel + "</td>" +
                "<td class='sub-td'>" + shortname + "</td>" +
                "<td class='sub-td'>" + display + "</td>" +
                "<td class='sub-td'>" +
                "<a href='edit?type=2&id=" + merchantCatVo.id + "' class='btn btn-info btn-mini'>编辑</a>" +
                //"<a href='javascript:void(0)' onclick='delCat("+merchantCatVo.id+")' class='btn btn-danger btn-mini sub-button'>删除</a>" +
                "</td>" +
                "</tr>";
            tdHtmls += tdHtml;
            if (trColor < 1) {
                trColor++;
            } else {
                trColor = 0;
            }
        }
        tdHtmls += "</tbody>" +
            "</table>";
        return tdHtmls;
    }

    // 表头
    function subTableThead() {
        var tableHTMl = "<table class='table sub-table' style='width:99%;margin-left:0.5%'>"
            + "<thead class='sub-thead'>"
            + "<tr class='sub-thead-tr'>"
            + "<th>序号</th>"
            + "<th>分类名称</th>"
            + "<th>分类名称（本地）</th>"
            + "<th>分类名称（英语）</th>"
            + "<th>简称</th>"
            + "<th>是否显示</th>"
            + "<th>操作</th>"
            + "</tr>"
            + "</thead>"
            + "<tbody class='sub-tbody'>";
        return tableHTMl;
    }

    // 分页
    var pageNum = 1;
    var pageTotalNum = 1;

    function subPagination(page) {
        pageTotalNum = page.totalPages;
        console.log(page);
        var pages = "";

        for (var i = 0; i < page.totalPages; i++) {
            if (pageNum == (i + 1)) {
                pages += "<li class='active'><a href='javascript:void(0)'>" + (i + 1) + "</a></li>";
            } else {
                pages += "<li><a href='javascript:void(0)' onclick='switchPage(" + (i + 1) + ",3)'>" + (i + 1) + "</a></li>";
            }
        }

        if (page.totalPages == 0) {
            pages += "<li class='active'><a href='javascript:void(0)'>1</a></li>";
        }

        var searchHtml = "<input placeholder='分类搜索' type='text' class='sub-search-input' id='subSearch' onchange='subSearch()'>";

        var pageHtml = "<div class='pagination ' style='float:right;'>" +
            "<ul total='1' index='1'><li>" +
            "<a href='javascript:void(0);' onclick='switchPage(1,3)'>首页</a>" +
            "</li><li><a href='javascript:void(0);' onclick='switchPage(0,1)'>«</a></li>" +
            pages +
            "<li><a href='javascript:void(0);' onclick='switchPage(0,2)',false>»</a></li><li>" +
            "<a href='javascript:void(0);' onclick='switchPage(" + page.totalPages + ",3)'>末页</a></li></ul>" +
            "</div>";
        pageHtml += searchHtml;
        return pageHtml;
    }

    // 删除
    function delCat(id) {
        if (confirm("确实要删除该商户分类吗?")) {
            sureDelCat(id);
        }
    }

    // 确认删除
    function sureDelCat(id) {
        if (confirm("确实要删除该商户分类吗?")) {
            var data = {id: id};
            $.ajax({
                url: 'delCat',
                data: JSON.stringify(data),
                type: 'post',
                async: false,
                dataType: 'json',
                contentType: 'application/json',
                success: function (result) {
                    if (result.code == 0) {
                        if (result.data) {
                            alert("删除成功");
                        } else {
                            alert("删除失败!请先删除对应的二级分类");
                        }
                    } else {
                        alert(result.msg);
                    }
                }
            });
        }
    }

    // 子集
    function subDataList(pcatId, subSearch) {
        var pcat = {id: pcatId};
        var data = {
            pcat: pcat,
            index: pageNum
        };
        if (subSearch != null) {
            data.name = subSearch;
        }
        var dataList;
        $.ajax({
            url: 'viewCat',
            data: JSON.stringify(data),
            type: 'post',
            async: false,
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    dataList = result.data;
                } else {
                    alert(result.msg);
                }
            }
        });
        return dataList;
    }

    // 切换分页
    function switchPage(page, fbSign) {
        if (fbSign == 1 && pageNum > 1 && page == 0) {
            console.log("上一页");
            pageNum -= 1;
        } else if (fbSign == 2 && pageNum < pageTotalNum && page == 0) {
            console.log("下一页");
            pageNum += 1;
        } else if (fbSign == 3) {
            pageNum = page;
        }
        var tableHTMl = subTableThead();
        var dataList = subDataList(pcatId);
        var tdHTMl = subTableTd(dataList);
        var pageHtml = subPagination(dataList);

        $("#tb-data").empty();
        $("#tb-data").html(tableHTMl + tdHTMl + pageHtml);

    }

    // 模糊查找
    function subSearch() {
        console.log($("#subSearch").val());
        var subSearch = $("#subSearch").val();
        var tableHTMl = subTableThead();
        var dataList = subDataList(pcatId, subSearch);
        var noResultHtml = "";
        if (dataList.totalPages == 0) {
            noResultHtml = "<div class='no-result-div' style='text-align: center;vertical-align: middle;height: 50px;line-height: 50px;'>" +
                "<p>暂无商户分类！</p>" +
                "</div>";
        }

        var tdHTMl = subTableTd(dataList);
        var pageHtml = subPagination(dataList);

        console.log("tableHTMl" + tableHTMl);
        console.log("tdHTMl" + tdHTMl);
        console.log("noResultHtml" + noResultHtml);
        console.log("pageHtml" + pageHtml);

        $("#tb-data").empty();
        $("#tb-data").html(tableHTMl + tdHTMl + noResultHtml + pageHtml);

        $("#subSearch").val(subSearch);
    }
});
