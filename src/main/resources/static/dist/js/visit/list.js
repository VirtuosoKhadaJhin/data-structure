/**
 * Created by young on 2017/9/1.
 */
$(document).ready(function () {
    changeCity(false);


    $(".btn-primary").on("click", function () {
        var reg = /^[0-9]*$/ ;
        var mchId = $("input[name=mchId]").val();
        if (!reg.test(mchId)) {
            alert("请输入正确的商户ID")
            return false;
        }
        var form = $(document.forms[0]);
        form.attr("action", "");
        form.attr("target", "");
    });
    $(".export").on("click", function () {
        var form = $(document.forms[0]);
        form.attr("action", "export");
        form.attr("target", "_blank");
        form.submit();
    });

    showLoading = function (a) {
        $(a).mask('<img src="/dist/img/loading.gif"/>')
    };

    hideLoading = function (a) {
        $(a).unmask();
    };

    $(".originalImg").load(function () {
        hideLoading ($(".show_img"));
    });

    $(".originalImg").error(function (e) {
        // hideLoading ($(".show_img"));
        $(".originalImg").attr('src',"/dist/img/errorimg.jpg") ;
    });


    $(".more_img").on("click", function () {
        openImgUrls(this.id);
    });
    $(".a_img").on("click", function () {
        openImgUrls(this.id);
    });
    var index;
    var imgArray = new Array();
    $(".left").on("click", function () {
        if (index > 1){
            //loading
            showLoading($(".show_img"));
            $(".originalImg").attr('src',imgArray[index-2]) ;
            index = index - 1;
            $("#img_index").text(index);
        }
    });
    $(".right").on("click", function () {
        if (index < imgArray.length){
            showLoading($(".show_img"));
            $(".originalImg").attr('src',imgArray[index]) ;
            index = index + 1;
            $("#img_index").text(index);
        }
    });
    function openImgUrls(imgs) {
        imgs = imgs.replace("[","").replace("]","");
        imgArray = imgs.split(",");
        showLoading($(".show_img"));
        $(".originalImg").attr('src',imgArray[0]) ;
        $("#img_index").text(1);
        $("#img_total").text(imgArray.length);
        index = parseInt($("#img_index").text());
        $(".approvalResultModel").modal("show");
    };

    // 搜索框重置
    $(".search-reset").click(function () {
        $(".search-form").find('input:text, input:password, input:file, select, textarea').val('');
        $(".search-form").find(".select2").val('').trigger('change');
    });


    $(".select-country").on("change", function () {
        changeCity(true);
        $(".district").html('<option value>全部</option>');
    });
    $(".select-city").on("change", function () {
        changeDistrict($(".select-city").val());
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

    function changeDistrict(cityId) {

        $.ajax({
            url: cityId+'/districts',
            data: null,
            type: 'get',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.code == 0) {
                    var html = '<option value>全部</option>';

                    if (result.data) {
                        result.data.forEach(function (dis) {
                            if ($("#hidden_districtId").val()&&$("#hidden_districtId").val() == dis.id){
                                html = html + '<option value="'+dis.id+'" selected>'+dis.name+'</option>';
                            }else {
                                html = html + '<option value="'+dis.id+'" >'+dis.name+'</option>';
                            }

                        });
                    }
                    $(".district").html(html);
                    $(".district").removeAttr("disabled");
                }
            }
        });
    }


});

