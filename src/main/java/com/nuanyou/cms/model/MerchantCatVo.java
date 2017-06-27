package com.nuanyou.cms.model;

/**
 * Created by mylon on 2017/6/19.
 */
public class MerchantCatVo {

    private Long id;

    // 分页
    private Integer index;

    // 名称
    private String name;

    // 简称
    private String shortname;

    // 排序
    private Boolean display;

    // 父节点
    private Long pcat;

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

    // convert keycode
    private String keycode;

    // convert keyCode
    private String keyCode;

    // convert shnKeyCode
    private String shnKeyCode;

    public MerchantCatVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Long getPcat() {
        return pcat;
    }

    public void setPcat(Long pcat) {
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

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public String getShnKeyCode() {
        return shnKeyCode;
    }

    public void setShnKeyCode(String shnKeyCode) {
        this.shnKeyCode = shnKeyCode;
    }
}
