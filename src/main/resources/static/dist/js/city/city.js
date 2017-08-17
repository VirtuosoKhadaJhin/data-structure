/**
 * Created by mylon on 2017/8/11.
 */

// 搜索框重置
$(".search-reset").click(function () {
    $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
    $(".search-form").find(".select2").val('').trigger('change');
});