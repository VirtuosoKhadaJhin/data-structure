package com.nuanyou.cms.entity.coupon;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/21.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_coupon")
public class Coupon {

    private Long id;
    private Long userId;
    private Long countryId;
    private String title;
    private String intro;
    private BigDecimal price;
    private BigDecimal localPrice;
    private Integer useRange;
    private BigDecimal startPrice;
    private BigDecimal startLocalPrice;
    private Long groupId;
    private String groupName;
    private Integer codeId;
    private String codeName;
    private Integer status;
    private Date validTime;
    private Date createTime;
    private Boolean appointMerchant;
    private String linkUrl;
    private boolean delete;

    private List<CouponMerchant> couponMerchantList;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "userid", nullable = true)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "countryid", nullable = true)
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


    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "localprice", nullable = true, precision = 2)
    public BigDecimal getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(BigDecimal localPrice) {
        this.localPrice = localPrice;
    }

    @Column(name = "userange", nullable = true)
    public Integer getUseRange() {
        return useRange;
    }

    public void setUseRange(Integer useRange) {
        this.useRange = useRange;
    }


    @Column(name = "startprice", nullable = true, precision = 2)
    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    @Column(name = "startlocalprice", nullable = true, precision = 2)
    public BigDecimal getStartLocalPrice() {
        return startLocalPrice;
    }

    public void setStartLocalPrice(BigDecimal startLocalPrice) {
        this.startLocalPrice = startLocalPrice;
    }

    @Column(name = "groupid", nullable = true)
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }


    @Column(name = "groupname", nullable = true, length = 50)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Column(name = "codeid", nullable = true)
    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }


    @Column(name = "codename", nullable = true, length = 100)
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }


    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Column(name = "validtime", nullable = true)
    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Column(name = "appointmerchant", nullable = true)
    public Boolean getAppointMerchant() {
        return appointMerchant;
    }

    public void setAppointMerchant(Boolean appointMerchant) {
        this.appointMerchant = appointMerchant;
    }


    @Column(name = "linkurl", nullable = true, length = 255)
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }


    @Column(name = "isdelete", nullable = true)
    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @OneToMany(fetch = FetchType.LAZY, targetEntity = CouponMerchant.class, cascade = CascadeType.ALL)
    @JoinColumns(value = {@JoinColumn(name = "couponid", referencedColumnName = "id")})
    public List<CouponMerchant> getCouponMerchantList() {
        return couponMerchantList;
    }

    public void setCouponMerchantList(List<CouponMerchant> couponMerchantList) {
        this.couponMerchantList = couponMerchantList;
    }

    public void addCouponMerchant(CouponMerchant couponMerchant) {
        if (couponMerchantList == null)
            couponMerchantList = new ArrayList<>(1);
        couponMerchantList.add(couponMerchant);
    }

}