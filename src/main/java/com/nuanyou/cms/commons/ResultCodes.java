package com.nuanyou.cms.commons;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public enum ResultCodes {

    Success(0, "成功"),

    Fail(1, "失败"),

    NotFound(404, "找不到页面"),

    MissingParameter(401, "缺少必要的参数"),

    NotFoundMerchant(402, "找不到该商户"),

    TypeMismatch(403, "参数格式不正确"),

    InvalidCaptcha(405, "错误的验证码"),

    SendCaptchaFail(406, "发送验证码失败"),

    UnameOrPwdError(407, "用户名或密码错误"),

    NotLogin(408, "未登录"),

    UsedName(409, "用户名已被使用"),

    UnkownError(500, "未知错误,请联系系统管理员"),

    NotImplemented(501, "未实现的方法"),

    NotFoundUser(502, "找不到用户"),

    NotFoundItem(503, "找不到该商品"),

    NotFoundCouponTemplate(504, "找不到模板"),

    NotFoundMerchantCard(505, "找不到代金券"),


    /**
     * ----------order -----------
     */
    Refunding(2001, "退款中"),
    RefundingSuccess(2003, "退款已经成功"),
    RefundingFail(2002, "退款已经失败"),
    Audited(2004, "已经审核完毕"),
    OrderNotFound(2005, "未发现订单"),
    OrderOther(2006, "其他订单错误");

    /**
     * ----------order -----------
     */


    public final Integer code;

    public final String message;

    ResultCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}