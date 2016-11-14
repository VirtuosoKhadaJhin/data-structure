package com.nuanyou.cms.model;

import java.util.List;

/**
 * Created by Felix on 2016/10/19.
 */
public class WithdrawPage {
    private Integer code ;
    private Integer total ;
    private Integer pages ;
    private List<Withdraw> data;
    private String msg;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<Withdraw> getData() {
        return data;
    }

    public void setData(List<Withdraw> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
