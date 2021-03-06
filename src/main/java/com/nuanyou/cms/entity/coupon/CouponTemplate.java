package com.nuanyou.cms.entity.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/21.
 */
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_coupon_template")
public class CouponTemplate {
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
    private String groupName;
    private Date createTime;

    public CouponTemplate() {
    }

    public CouponTemplate(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "countryid", nullable = false)
    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Column(name = "title", nullable = true, length = 40)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Column(name = "intro", nullable = true, length = 200)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Convert(converter = CouponTemplateConverter.class)
    @Column(name = "type", nullable = true)
    public CouponTemplateType getType() {
        return type;
    }

    public void setType(CouponTemplateType type) {
        this.type = type;
    }

    @Convert(converter = JsonListConverter.class)
    @Column(name = "merchants", nullable = true)
    public List<String> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<String> merchants) {
        this.merchants = merchants;
    }

    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "localprice", nullable = true)
    public BigDecimal getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(BigDecimal localPrice) {
        this.localPrice = localPrice;
    }

    @Column(name = "availabledays", nullable = true)
    public Integer getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Integer availableDays) {
        this.availableDays = availableDays;
    }


    @Column(name = "userange", nullable = true)
    @Convert(converter = UserRangeConverter.class)
    public List<UserRange> getUserRanges() {
        return userRanges;
    }

    public void setUserRanges(List<UserRange> userRanges) {
        this.userRanges = userRanges;
    }


    @Column(name = "startprice", nullable = true, precision = 2)
    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    @Column(name = "startlocalprice", nullable = true)
    public BigDecimal getStartLocalPrice() {
        return startLocalPrice;
    }

    public void setStartLocalPrice(BigDecimal startLocalPrice) {
        this.startLocalPrice = startLocalPrice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    public CouponGroup getCouponGroup() {
        return couponGroup;
    }

    public void setCouponGroup(CouponGroup couponGroup) {
        this.couponGroup = couponGroup;
    }


    @Column(name = "groupname", nullable = true, length = 50)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
