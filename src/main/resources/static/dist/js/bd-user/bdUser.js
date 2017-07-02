/**
 * Created by mylon on 2017/7/2.
 */

// 重置搜索框
$(".search-reset").click(function () {
    $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
    $(".search-form").find(".select2").val('').trigger('change');
});

