$(".pagination ul").each(function (i, o) {
    var total = parseInt($(this).attr("total"));
    var totalelements = parseInt($(this).attr("totalelements"));
    var index = parseInt($(this).attr("index"));
    if (total == 0)
        return;

    localStorage['index'] = index;

    var size = total < 10 ? total : 10;

    var left = total < 10 ? 1 : index - Math.floor(size / 2);
    left = left > 1 ? left : 1;

    var right = left - 1 + size;
    right = right < total ? right : total;

    var isFirst = index == 1;
    var isLast = index == total;

    var htm = '';
    if (totalelements) {
        htm = '<li><span>共'+totalelements+'条，每页20条</span></li>';
    }
    htm += build(1, "首页", isFirst);
    htm += build(index - 1, "«", isFirst);

    for (var i = left; i <= right; i++)
        htm += build(i, i, null, i == index ? "active" : "");

    htm += build(index + 1, "»", isLast);
    htm += build(total, "末页", isLast);
    $(this).html(htm);
});

function build(index, show, disable, style) {
    index = new UrlBuilder().set("index", index).build();

    if (disable == true) {
        return '<li><a href="javascript:void(0);">' + show + '</a></li>';
    } else if (disable == false) {
        return '<li><a href="' + index + '">' + show + '</a></li>';
    } else {
        return '<li class="' + style + '"><a href="' + index + '">' + show + '</a></li>';
    }
}

function sort(propertie, direction) {
    var search = new UrlBuilder().set("propertie", propertie).set("direction", direction).build();
    window.location.href = window.location.href.split("?")[0] + search;
}


function UrlBuilder() {
    var params = window.location.search;
    if (params) {
        params = params.replace("?", "");
        params = params.split("&");
    } else {
        params = [];
    }

    this.set = function (key, value) {
        for (var p in params) {
            var entry = params[p].split("=");
            if (entry[0] == key) {
                params[p] = key + "=" + value;
                return this;
            }
        }
        params.push(key + "=" + value);
        return this;
    };

    this.build = function () {
        var result = "";
        for (var p in params)
            result += "&" + params[p];

        result = result.replace("&", "?");
        return result;
    };

}