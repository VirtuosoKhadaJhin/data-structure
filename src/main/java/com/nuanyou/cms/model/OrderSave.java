package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by yangkai on 2016/8/16.
 */
public class OrderSave {
    private Long id;

    private String linkUrl;

    private BigDecimal price;

    private BigDecimal oPrice;

    private BigDecimal localPrice;

    private BigDecimal oLocalPrice;

    private Long countryId;

    private String orderSn;

    private String mchOrderSn;

    @JsonProperty("orderid")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 活动跳转地址
     **/
    @JsonProperty("linkurl")
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * 现价
     **/
    @JsonProperty("price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 原价
     **/
    @JsonProperty("oprice")
    public BigDecimal getoPrice() {
        return oPrice;
    }

    public void setoPrice(BigDecimal oPrice) {
        this.oPrice = oPrice;
    }


    /**
     * 本地现价
     **/
    @JsonProperty("localprice")
    public BigDecimal getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(BigDecimal localPrice) {
        this.localPrice = localPrice;
    }

    /**
     * 本地原价
     **/
    @JsonProperty("olocalprice")
    public BigDecimal getoLocalPrice() {
        return oLocalPrice;
    }

    public void setoLocalPrice(BigDecimal oLocalPrice) {
        this.oLocalPrice = oLocalPrice;
    }

    /**
     * 国家id
     **/
    @JsonProperty("countryid")
    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    /**
     * 暖游订单编号
     **/
    @JsonProperty("ordersn")
    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 商户订单编号
     **/
    @JsonProperty("mchordersn")
    public String getMchOrderSn() {
        return mchOrderSn;
    }

    public void setMchOrderSn(String mchOrderSn) {
        this.mchOrderSn = mchOrderSn;
    }

}