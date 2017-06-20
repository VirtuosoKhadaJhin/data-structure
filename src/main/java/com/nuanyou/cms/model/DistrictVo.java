package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;

import java.math.BigDecimal;

/**
 * Created by mylon on 2017/6/20.
 */
public class DistrictVo {

    private Long id;

    // 用来接收keyCode
    private String name;

    // 中文名称
    private String nameLabel;

    // 简称
    private String shortname;

    // 汇率系数
    private BigDecimal radio;

    // 是否显示
    private Boolean display;

    // 城市
    private City city;

    // 国家
    private Country country;

    // 排序
    private Integer sort;

    // 跳转链接
    private String link;

    // 英文
    private String enNameLabel;

    // 本地语言
    private String localNameLabel;

    public DistrictVo() {
    }

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

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public BigDecimal getRadio() {
        return radio;
    }

    public void setRadio(BigDecimal radio) {
        this.radio = radio;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
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

    public String getEnNameLabel() {
        return enNameLabel;
    }

    public void setEnNameLabel(String enNameLabel) {
        this.enNameLabel = enNameLabel;
    }

    public String getLocalNameLabel() {
        return localNameLabel;
    }

    public void setLocalNameLabel(String localNameLabel) {
        this.localNameLabel = localNameLabel;
    }
}
