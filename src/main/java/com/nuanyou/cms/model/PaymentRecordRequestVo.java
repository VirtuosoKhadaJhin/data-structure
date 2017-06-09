package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.enums.PaymentOrderMethod;
import com.nuanyou.cms.entity.enums.PaymentOrderStatus;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Byron on 2017/6/9.
 */
public class PaymentRecordRequestVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String tradeNo;

    private String orderNo;

    private PaymentOrderStatus status;

    private BigDecimal price;

    private String app;

    private PaymentOrderMethod method;

    private Date payTime;

    private Data refundTime;

    private Date createTime;

    private Integer index = 1;

    private Integer pageNum = 20;

    private Date beginDt;

    private Date endDt;

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Date getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(Date beginDt) {
        this.beginDt = beginDt;
    }

    public Date getEndDt() {
        return endDt;
    }

    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }
}
