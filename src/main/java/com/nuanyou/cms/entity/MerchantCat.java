package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_merchant_cat")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class MerchantCat {
    private Long id;
    private String name;
    private String shortname;
    private String kpname;
    private Boolean display;
    private Integer sort;
    private Date createtime;
    private Date updatetime;
    private MerchantCat pcat;
    private String imageUrl;
    private String mapimgurl;
    // keycode
    private String keyCode;
    // shortname keycode
    private String shnKeyCode;


    public MerchantCat(String name, String shortname, Boolean display, Integer sort, String imageUrl, String mapimgurl) {
        this.name = name;
        this.shortname = shortname;
        this.display = display;
        this.sort = sort;
        this.imageUrl = imageUrl;
        this.mapimgurl = mapimgurl;
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


    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "shortname", nullable = true, length = 50)
    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Column(name = "keycode", nullable = false, length = 100)
    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    @Column(name = "shnkeycode", nullable = false, length = 100)
    public String getShnKeyCode() {
        return shnKeyCode;
    }

    public void setShnKeyCode(String shnKeyCode) {
        this.shnKeyCode = shnKeyCode;
    }

    @Column(name = "kpname", nullable = true, length = 50)
    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }

    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }


    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @CreatedAt
    @Column(name = "updatetime", nullable = true)
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", nullable = true)
    @JsonIgnore
    public MerchantCat getPcat() {
        return pcat;
    }

    public void setPcat(MerchantCat pcat) {
        this.pcat = pcat;
    }

    @Column(name = "imgurl", nullable = true, length = 250)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "mapimgurl", nullable = true, length = 250)
    public String getMapimgurl() {
        return mapimgurl;
    }

    public void setMapimgurl(String mapimgurl) {
        this.mapimgurl = mapimgurl;
    }

    public MerchantCat(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MerchantCat(Long id) {
        this.id = id;
    }

    public MerchantCat() {
    }
}
