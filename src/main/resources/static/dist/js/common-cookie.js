/**
 * Created by Byron on 2017/7/10.
 */
function setSessionCookie(cookieKey, cookieValue, exdays) {
    var d = navigator.userAgent.indexOf("MSIE") > 0;
    if (d) {//IE内核
        if (cookieValue) {
            var a = "; expires=At the end of the Session";
            var f = "";
            if (exdays != null) {
                f = "; path=" + exdays
            }
            document.cookie = cookieKey + "=" + escape(cookieValue) + a + f
        }
    } else {
        if (cookieValue) {
            var a = "; expires=Session";
            var f = "";
            if (exdays != null) {
                f = "; path=" + exdays
            }
            document.cookie = cookieKey + "=" + escape(cookieValue) + a + f
        }
    }
}
function getCookieValue(cookieKey) {
    var cookieValue = document.cookie;
    var a = cookieValue.indexOf("" + cookieKey + "=");
    if (a == -1) {
        a = cookieValue.indexOf(cookieKey + "=")
    }
    if (a == -1) {
        cookieValue = null
    } else {
        a = cookieValue.indexOf("=", a) + 1;
        cookieEndAt = cookieValue.indexOf(";", a);
        if (cookieEndAt == -1) {
            cookieEndAt = cookieValue.length
        }
        cookieValue = unescape(cookieValue.substring(a, cookieEndAt))
    }
    return cookieValue
}