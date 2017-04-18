package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;
import com.nuanyou.cms.entity.enums.FeatureCat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Felix on 2016/9/29.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_feature")
public class Feature {
    private Long id;
    private String title;
    private String imgurl;
    private String linkurl;
    private String content;
    private Byte type;
    private FeatureCat cat;
    private City city;
    private Country country;
    private Boolean display;
    private Date updatetime;
    private Date createtime;
    private Integer readnum;
    private Integer likenum;
    private Integer sort;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Column(name = "imgurl", nullable = true, length = 255)
    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    @Column(name = "linkurl", nullable = true, length = 255)
    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }


    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Column(name = "type", nullable = true)
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Convert(converter = FeatureCat.Converter.class)
    @Column(name = "cat")
    public FeatureCat getCat() {
        return cat;
    }

    public void setCat(FeatureCat cat) {
        this.cat = cat;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityid", nullable = true)
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid", nullable = true)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }


    @LastModified
    @Column(name = "updatetime", nullable = true)
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    @Column(name = "readnum", nullable = true)
    public Integer getReadnum() {
        return readnum;
    }

    public void setReadnum(Integer readnum) {
        this.readnum = readnum;
    }


    @Column(name = "likenum", nullable = true)
    public Integer getLikenum() {
        return likenum;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }

    @OrderBy("ASC")
    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
