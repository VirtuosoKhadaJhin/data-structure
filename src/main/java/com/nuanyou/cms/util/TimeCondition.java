package com.nuanyou.cms.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Felix on 2016/9/10.
 */
public class TimeCondition {

    private Date begin;

    private Date end;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getBeginDate() {
        return begin;
    }

    public void setBeginDate(Date begin) {
        this.begin = begin;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return end;
    }

    public void setEndDate(Date end) {
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
