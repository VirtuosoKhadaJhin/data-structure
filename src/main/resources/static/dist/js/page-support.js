$(".pagination ul").each(function (i, o) {
    var total = parseInt($(this).attr("total"));
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

    var htm = build(1, "首页", isFirst);
    htm += build(index - 1, "«", isFirst);

    for (var i = left; i <= right; i++)
        htm += build(i, i, null, i == index ? "active" : "");

    htm += build(index + 1, "»", isLast);
    htm += build(total, "末页", isLast);
    $(this).html(htm);
});

function build(index, show, disable, style) {
    index = "?index=" + index + window.location.search.replace(/^[?]/, "&").replace(/[?|&]?index=[^&]*/, "");

    if (disable == true) {
        return '<li><a href="javascript:void(0);">' + show + '</a></li>';
    } else if (disable == false) {
        return '<li><a href="' + index + '">' + show + '</a></li>';
    } else {
        return '<li class="' + style + '"><a href="' + index + '">' + show + '</a></li>';
    }
}
