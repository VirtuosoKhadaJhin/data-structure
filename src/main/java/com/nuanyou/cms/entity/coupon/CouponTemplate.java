package com.nuanyou.cms.entity.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.UserRange;
import com.nuanyou.cms.entity.enums.UserRangeConverter;

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
    private String title;
    private String intro;
    private BigDecimal price;
    private Integer availabledays;
    private List<UserRange> userRanges;
    private BigDecimal startprice;
    private CouponGroup couponGroup;
    private String groupname;
    private Date createtime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    @Column(name = "availabledays", nullable = true)
    public Integer getAvailabledays() {
        return availabledays;
    }

    public void setAvailabledays(Integer availabledays) {
        this.availabledays = availabledays;
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
    public BigDecimal getStartprice() {
        return startprice;
    }

    public void setStartprice(BigDecimal startprice) {
        this.startprice = startprice;
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
    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Transient
    public Long getCouponGroupId(){
        return couponGroup.getId();
    }


}
