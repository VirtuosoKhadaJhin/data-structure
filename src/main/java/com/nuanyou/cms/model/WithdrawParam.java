package com.nuanyou.cms.model;

import com.nuanyou.cms.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Felix on 2016/10/20.
 */
public class WithdrawParam {
    private String userid;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;


    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }


    public void setBegin(Date begin) {
        this.begin = begin;
    }


    public void setEnd(Date end) {
        this.end = end;
    }


    public String getEndStr() {
        if (end == null) {
            return null;
        }
        return DateUtils.format(end);
    }


    public String getBeginStr() {
        if (begin == null) {
            return null;
        }
        return DateUtils.format(begin);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
