package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.coupon.CouponGroup;
import com.nuanyou.cms.entity.enums.CouponTemplateType;
import com.nuanyou.cms.entity.enums.UserRange;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/21.
 */
public class CouponTemplateVO {
    private Long id;
    private Long countryId;
    private String title;
    private String intro;
    private CouponTemplateType type;
    private List<String> merchants;
    private BigDecimal price;
    private BigDecimal localPrice;
    private Integer availableDays;
    private List<UserRange> userRanges;
    private BigDecimal startPrice;
    private BigDecimal startLocalPrice;
    private CouponGroup couponGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    public CouponTemplateType getType() {
        return type;
    }

    public void setType(CouponTemplateType type) {
        this.type = type;
    }

    public List<String> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<String> merchants) {
        this.merchants = merchants;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public BigDecimal getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(BigDecimal localPrice) {
        this.localPrice = localPrice;
    }


    public Integer getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Integer availableDays) {
        this.availableDays = availableDays;
    }


    public List<UserRange> getUserRanges() {
        return userRanges;
    }

    public void setUserRanges(List<UserRange> userRanges) {
        this.userRanges = userRanges;
    }


    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }


    public BigDecimal getStartLocalPrice() {
        return startLocalPrice;
    }

    public void setStartLocalPrice(BigDecimal startLocalPrice) {
        this.startLocalPrice = startLocalPrice;
    }


    public CouponGroup getCouponGroup() {
        return couponGroup;
    }

    public void setCouponGroup(CouponGroup couponGroup) {
        this.couponGroup = couponGroup;
    }

}
