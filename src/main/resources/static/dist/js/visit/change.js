/**
 * Created by young on 2017/9/19.
 */

$(document).ready(function () {

    $(".change").on("click", function () {
        var item = this.outerText;
        var originalvalue = this.dataset.originalvalue;
        var currentvalue = this.dataset.currentvalue;

        var type = this.dataset.type;
        var multi = this.dataset.multi;

        setValue(originalvalue,type,multi, $(".changeResultModel .originalvalue"));
        setValue(currentvalue,type,multi,$(".changeResultModel .currentvalue"));


        $(".changeResultModel .item").text(item);


        $(".changeResultModel").modal("show");
    });

    function setValue(value,type,multi,dom) {
        if (value) {
            if (multi == 'true') {
                value = value.replace("[","").replace("]","");
                var array = value.split(",");
                var vals = '';
                array.forEach(function (val) {
                    if (type == 'text') {
                        vals = vals + val +'&nbsp;&nbsp;&nbsp;';
                    }else {
                        vals = vals + '<img  style="width:160px;height: 100px" src="'+val+'">&nbsp;&nbsp;&nbsp;';
                    }
                });
                dom.html(vals);
            }else {
                if (type == 'text') {
                    dom.html(value);
                }else {
                    var value_img = '<img  style="width:160px;height: 100px" src="'+value+'">&nbsp;&nbsp;&nbsp;';
                    dom.html(value_img);
                }
            }
        }
    }
});

