package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Byron on 2017/6/12.
 */
public class CodePayResponse {

    /**
     * 0.支付成功，1.等待密码, 2.重复订单, 3.失败
     */
    @JsonProperty("status")
    private int status;
    /**
     * 人民币金额
     */
    @JsonProperty("rmbprice")
    private BigDecimal rmbprice;
    /**
     * 支付流水号
     */
    @JsonProperty("orderid")
    private Long orderid;
    /**
     * 支付订单号
     */
    @JsonProperty("tradeno")
    private String tradeno;
    /**
     * 通道流水号
     */
    @JsonProperty("transactionid")
    private String transactionid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getRmbprice() {
        return rmbprice;
    }

    public void setRmbprice(BigDecimal rmbprice) {
        this.rmbprice = rmbprice;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }
}
