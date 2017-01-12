package com.nuanyou.cms.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Felix on 2016/9/10.
 */
public class TimeCondition {

    private Date begin;

    private Date end;

    private Date begin_2;

    private Date end_2;


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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBegin_2() {
        return begin_2;
    }

    public void setBegin_2(Date begin_2) {
        this.begin_2 = begin_2;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEnd_2() {
        return end_2;
    }

    public void setEnd_2(Date end_2) {
        this.end_2 = end_2;
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


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getBegin_2Date() {
        return begin_2;
    }

    public void setBegin_2Date(Date begin) {
        this.begin_2 = begin_2;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getEnd_2Date() {
        return end_2;
    }

    public void setEnd_2Date(Date end) {
        this.end_2 = end_2;
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
    public String getEnd_2Str() {
        if (end_2 == null) {
            return null;
        }
        return DateUtils.format(end_2);
    }


    public String getBegin_2Str() {
        if (begin_2 == null) {
            return null;
        }
        return DateUtils.format(begin_2);
    }


}
