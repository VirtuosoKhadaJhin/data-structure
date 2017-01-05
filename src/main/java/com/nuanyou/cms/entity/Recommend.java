package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_recommend")
public class Recommend {

    private Long id;
    private String linkUrl;
    private String imgUrl;
    private Integer type;
    private String title;
    private Long referId;
    private RecommendCat cat;
    private Integer sort;
    private Boolean display;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "linkurl", nullable = true, length = 100)
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }


    @Column(name = "imgurl", nullable = true, length = 100)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Column(name = "type", nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @Column(name = "title", nullable = true, length = 40)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Column(name = "referid", nullable = true)
    public Long getReferId() {
        return referId;
    }

    public void setReferId(Long referId) {
        this.referId = referId;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catid", nullable = true)
    public RecommendCat getCat() {
        return cat;
    }

    public void setCat(RecommendCat cat) {
        this.cat = cat;
    }

    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

}