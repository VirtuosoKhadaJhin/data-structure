package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.enums.PaymentOrderMethod;
import com.nuanyou.cms.entity.enums.PaymentOrderStatus;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Byron on 2017/6/9.
 */
public class PaymentOrderRecordVo {

    private Long id;

    private String title;

    //支付宝和微信流水号
    private String tradeNo;

    //暖游NYOrderID；
    private String orderNo;

    //支付状态
    private PaymentOrderStatus status;

    //支付价格
    private BigDecimal price;

    //支付平台
    private String app;

    //支付方式
    private PaymentOrderMethod method;

    private Date payTime;

    private Data refundTime;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public PaymentOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentOrderStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public PaymentOrderMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentOrderMethod method) {
        this.method = method;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Data getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Data refundTime) {
        this.refundTime = refundTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
