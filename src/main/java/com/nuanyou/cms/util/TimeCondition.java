package com.nuanyou.cms.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Felix on 2016/9/10.
 */
public class TimeCondition {
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
}
