package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.MerchantCat;

/**
 * Created by mylon on 2017/6/19.
 */
public class MerchantCatVo {

    private Long id;

    // keycode
    private String name;

    // 名称
    private String nameLabel;

    // 简称
    private String shortname;

    // 排序
    private Boolean display;

    // 父节点
    private MerchantCat pcat;

    // 排序
    private Integer sort;

    // 背景图
    private String imageUrl;

    // 地图标志
    private String mapimgurl;

    // 英文
    private String enNameLabel;

    // 本地语言
    private String localNameLabel;

    public MerchantCatVo() {
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

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public MerchantCat getPcat() {
        return pcat;
    }

    public void setPcat(MerchantCat pcat) {
        this.pcat = pcat;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMapimgurl() {
        return mapimgurl;
    }

    public void setMapimgurl(String mapimgurl) {
        this.mapimgurl = mapimgurl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
