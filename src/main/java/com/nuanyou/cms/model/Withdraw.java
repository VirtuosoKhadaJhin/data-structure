package com.nuanyou.cms.model;

import java.util.Date;

/**
 * Created by Felix on 2016/10/19.
 */
public class Withdraw {


    private Long id;
    private Long userid;
    private String nickname;
    private String bank;
    private String name;
    private String account;
    private String amount;
    private Integer status;
    private Long createtime;
    private String message;
    private Long approvetime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        if(createtime==null){
            return null;
        }
        return new Date(Long.valueOf(createtime) * 1000);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Date getApprovetime() {
        if(approvetime==null){
            return null;
        }
        return new Date(Long.valueOf(approvetime) * 1000);
    }

    public void setApprovetime(Long approvetime) {
        this.approvetime = approvetime;
    }
}
