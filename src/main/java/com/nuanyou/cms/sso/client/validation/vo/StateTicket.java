package com.nuanyou.cms.sso.client.validation.vo;

import java.util.Date;

/**
 * Created by Felix on 2017/7/3.
 */
public class StateTicket {

    private String code;
    private Date createTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StateTicket(String code, Date createTime) {
        this.code = code;
        this.createTime = createTime;
    }

    public boolean isExpired() {
        return (this == null)||
                (System.currentTimeMillis() - this.createTime.getTime() >= 20*1000);
    }
}
