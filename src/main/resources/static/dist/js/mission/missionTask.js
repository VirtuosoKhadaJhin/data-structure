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

    $(".th-checked").on("click", function () {
        if(this.checked){
            $(".tbody-list :checkbox").prop("checked", true);
        }else{
            $(".tbody-list :checkbox").prop("checked", false);
        }
    });

});
