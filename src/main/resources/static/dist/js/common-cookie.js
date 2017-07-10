/**
 * Created by Byron on 2017/7/10.
 */

/**
 * 添加cookie
 * @param cookieKey
 * @param cookieValue
 * @param exdays
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

/**
 * 获取cookie值
 * @param cookieKey
 * @returns {string}
 */
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

/**
 * 获取URL中参数值
 * @param name
 * @returns {null}
 */
function getUrlParamValue(url, key) {
    var reg = new RegExp("(^|[&?])" + key + "=([^&]*)(&|$)");
    var r = url.match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

/**
 * 替换URL参数值
 * @param url
 * @param paramName
 * @param replaceWith
 * @returns {*|XML|string|void}
 */
function replaceUrlParamVal(url, paramName, replaceWith) {
    var re = eval('/(' + paramName + '=)([^&]*)/gi');
    var nUrl = url.replace(re, paramName + '=' + replaceWith);
    return nUrl;
}