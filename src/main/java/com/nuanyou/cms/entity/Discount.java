package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.DiscountType;
import com.nuanyou.cms.entity.enums.DiscountTypeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/11/15.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_discount")
public class Discount {

    private Long id;
    private String title;
    private Country country;
    private City city;
    private String imgUrl;
    private DiscountType type;
    private Long relatedId;
    private String intro;
    private String linkUrl;
    private Date startTime;
    private Date endTime;
    private boolean deleted;
    private Date createTime;

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "countryid", nullable = true)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cityid", nullable = true)
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    @Column(name = "imgurl", nullable = true, length = 200)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Convert(converter = DiscountTypeConverter.class)
    @Column(name = "type", nullable = true)
    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }


    @Column(name = "relatedid", nullable = true)
    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }


    @Column(name = "intro", nullable = true, length = -1)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    @Column(name = "linkurl", nullable = true, length = 200)
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "starttime", nullable = true)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "endtime", nullable = true)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "deleted", nullable = true)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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