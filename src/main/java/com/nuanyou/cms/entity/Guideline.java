package com.nuanyou.cms.entity;

import com.nuanyou.cms.entity.enums.GuidelineType;
import com.nuanyou.cms.entity.enums.GuidelineTypeConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@Table(name = "ny_guideline")
public class Guideline {

    private Long id;
    private String name;
    private String imgUrl;
    private String icon;
    private String url;
    private Integer sort;
    private List<GuidelineType> type;
    private City city;
    private Long countryId;
    private Long districtId;
    private String pushTitle;
    private String pushIntro;
    private String pushImgUrl;
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


    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "imgurl", nullable = true, length = 255)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Column(name = "icon", nullable = true, length = 255)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Convert(converter = GuidelineTypeConverter.class)
    @Column(name = "type", nullable = true)
    public List<GuidelineType> getType() {
        return type;
    }

    public void setType(List<GuidelineType> type) {
        this.type = type;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cityid", nullable = true)
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Column(name = "countryid", nullable = true)
    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }


    @Column(name = "districtid", nullable = true)
    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    @Column(name = "pushtitle", nullable = true, length = 200)
    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }


    @Column(name = "pushintro", nullable = true, length = 255)
    public String getPushIntro() {
        return pushIntro;
    }

    public void setPushIntro(String pushIntro) {
        this.pushIntro = pushIntro;
    }

    @Column(name = "pushimgurl", nullable = true, length = 255)
    public String getPushImgUrl() {
        return pushImgUrl;
    }

    public void setPushImgUrl(String pushImgUrl) {
        this.pushImgUrl = pushImgUrl;
    }

}