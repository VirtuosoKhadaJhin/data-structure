package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Byron on 2017/6/8.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "payment_order")
public class PaymentOrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "tradeno")
    private String tradeNo;//支付宝和微信流水号

    @Column(name = "tradeno")
    private String orderNo; //暖游NYOrderID；

    @Column(name = "status")
    private Integer status; //支付状态

    @Column(name = "price")
    private BigDecimal price;//支付价格

    @Column(name = "app")
    private String app; //支付平台

    @Column(name = "method")
    private Integer method;

    @Column(name = "paytime")
    private Date payTime;

    @Column(name = "refundtime")
    private Data refundTime;

    @Column(name = "createtime")
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
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
