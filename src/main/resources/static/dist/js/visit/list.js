/**
 * Created by young on 2017/9/1.
 */
$(document).ready(function () {
    changeCity(false);


    $(".more_img").on("click", function () {
        openImgUrls(this.id);
    });
    function openImgUrls(imgs) {
        imgs = imgs.replace("[","").replace("]","");
        var imgArray = new Array();
        imgArray = imgs.split(",");
        $(".originalImg").attr('src',imgArray[0]) ;
        $("#img_index").text(1);
        $("#img_total").text(imgArray.length);
        var index = parseInt($("#img_index").text());
        $(".left").on("click", function () {
            if (index > 1){
                $(".originalImg").attr('src',imgArray[index]) ;
                index = index - 1;
                $("#img_index").text(index);
            }
        });
        $(".right").on("click", function () {
            if (index < imgArray.length){
                $(".originalImg").attr('src',imgArray[index]) ;
                index = index + 1;
                $("#img_index").text(index);
            }
        });
        $(".approvalResultModel").modal("show");
    };

    // 搜索框重置
    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });


    $(".select-country").on("change", function () {
        changeCity(true);
    });

    function changeCity(isReset) {
        var countryId = $(".select-country").val();
        var cityOptions = $(".option-city");
        for (var i = 0; i < cityOptions.length; i++) {
            var cityOption = cityOptions[i];
            if (countryId != $(cityOption).attr("country-id")) {
                $(cityOption).hide();
            } else {
                $(cityOption).show();
            }
        }
        if (countryId != "") {
            $(".select-city").removeAttr("disabled");
        } else {
            $(".select-city").attr("disabled", "disabled");
        }
        if (isReset) {
            $("#city").val("");
        }
    }


});

