package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.PaymentOrderMethod;
import com.nuanyou.cms.entity.enums.PaymentOrderStatus;

import javax.persistence.*;
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

    @Column(name = "mchid")
    private Long mchId;

    @Column(name = "mchname")
    private String mchName;

    @Column(name = "mchkpname")
    private String mchKpName;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private PaymentOrderStatus status; //支付状态

    @Column(name = "userid")
    private Long userId;

    @Column(name = "transactionid")
    private String transactionId;

    @Column(name = "outtradeno")
    private String outTradeNo;//暖游对外订单号

    @Column(name = "tradeno")
    private String tradeNo;

    @Column(name = "orderno")
    private String orderNo; //暖游NYOrderID；

    @Column(name = "app")
    private String app; //支付平台

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;//支付价格

    @Column(name = "realprice")
    private BigDecimal realPrice;

    @Column(name="method")
    @Enumerated(EnumType.ORDINAL)
    private PaymentOrderMethod method;

    @Column(name = "chnid")
    private String channelId;

    @Column(name = "channelname")
    private String channelName;//支付通道

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "realrate")
    private BigDecimal realRate;

    @Column(name = "paytime")
    private Date payTime;

    @Column(name = "refundtime")
    private Date refundTime;

    @Column(name = "createtime")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public PaymentOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentOrderStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public PaymentOrderMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentOrderMethod method) {
        this.method = method;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRealRate() {
        return realRate;
    }

    public void setRealRate(BigDecimal realRate) {
        this.realRate = realRate;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
