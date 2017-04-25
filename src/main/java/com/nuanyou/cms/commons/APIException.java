package com.nuanyou.cms.commons;


/**
 * Created by Alan.ye on 2016/9/7.
 */
public class APIException extends RuntimeException {

    private int code;

    public APIException(ResultCodes code) {
        super(code.message);
        this.code = code.code;
    }

    public APIException(ResultCodes code, Object message) {
        super(code.message + "：" + String.valueOf(message));
        this.code = code.code;
    }

    public APIException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
