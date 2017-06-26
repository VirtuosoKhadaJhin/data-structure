package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.enums.PaymentOrderMethod;
import com.nuanyou.cms.entity.enums.PaymentOrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Byron on 2017/6/9.
 */
public class PaymentRecordRequestVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mchId;

    private String mchName;

    private String mchKpName;

    private String title;

    private String tradeNo;

    private String outTradeNo;

    private PaymentOrderStatus status;

    private PaymentOrderMethod paymentOrderMethod;

    private String payChannel;

    private BigDecimal beginPrice;

    private BigDecimal endPrice;

    //支付时间begin
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDt;

    //支付时间end
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDt;

    private Integer index = 1;

    private Integer pageNum = 20;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getMchKpName() {
        return mchKpName;
    }

    public void setMchKpName(String mchKpName) {
        this.mchKpName = mchKpName;
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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public PaymentOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentOrderStatus status) {
        this.status = status;
    }

    public PaymentOrderMethod getPaymentOrderMethod() {
        return paymentOrderMethod;
    }

    public void setPaymentOrderMethod(PaymentOrderMethod paymentOrderMethod) {
        this.paymentOrderMethod = paymentOrderMethod;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public BigDecimal getBeginPrice() {
        return beginPrice;
    }

    public void setBeginPrice(BigDecimal beginPrice) {
        this.beginPrice = beginPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
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
}
