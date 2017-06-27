package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by mylon on 2017/6/20.
 */
public class DistrictVo {

    private Long id;

    private String name;

    private String shortname;

    private String localname;

    private String enname;

    private Double startlongitude;

    private Double endlongitude;

    private Double startlatitude;

    private Double endlatitude;

    private String indeximgurl;

    private String bgimgurl;

    private Boolean display;

    private Integer sort;

    private String link;

    private String icon;

    private Date createtime;

    private Date updatetime;

    private City city;

    private Country country;

    private BigDecimal radio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getLocalname() {
        return localname;
    }

    public void setLocalname(String localname) {
        this.localname = localname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public Double getStartlongitude() {
        return startlongitude;
    }

    public void setStartlongitude(Double startlongitude) {
        this.startlongitude = startlongitude;
    }

    public Double getEndlongitude() {
        return endlongitude;
    }

    public void setEndlongitude(Double endlongitude) {
        this.endlongitude = endlongitude;
    }

    public Double getStartlatitude() {
        return startlatitude;
    }

    public void setStartlatitude(Double startlatitude) {
        this.startlatitude = startlatitude;
    }

    public Double getEndlatitude() {
        return endlatitude;
    }

    public void setEndlatitude(Double endlatitude) {
        this.endlatitude = endlatitude;
    }

    public String getIndeximgurl() {
        return indeximgurl;
    }

    public void setIndeximgurl(String indeximgurl) {
        this.indeximgurl = indeximgurl;
    }

    public String getBgimgurl() {
        return bgimgurl;
    }

    public void setBgimgurl(String bgimgurl) {
        this.bgimgurl = bgimgurl;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BigDecimal getRadio() {
        return radio;
    }

    public void setRadio(BigDecimal radio) {
        this.radio = radio;
    }
}
