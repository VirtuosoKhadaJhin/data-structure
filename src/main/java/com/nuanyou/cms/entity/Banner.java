package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Felix on 2016/9/27.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_banner")
public class Banner {
    private Long id;
    private String bannerurl;
    private String appbannerurl;
    private String linkurl;
    private Integer sort;
    private Byte userange;
    private Date starttime;
    private Date endtime;
    private Integer showcount;
    private Country country;
    private String title;
    private String page;
    private City city;
    private Boolean ishot;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "bannerurl", nullable = false, length = 255)
    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl;
    }


    @Column(name = "appbannerurl", nullable = true, length = 255)
    public String getAppbannerurl() {
        return appbannerurl;
    }

    public void setAppbannerurl(String appbannerurl) {
        this.appbannerurl = appbannerurl;
    }


    @Column(name = "linkurl", nullable = true, length = 255)
    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }


    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Column(name = "userange", nullable = true)
    public Byte getUserange() {
        return userange;
    }

    public void setUserange(Byte userange) {
        this.userange = userange;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityid")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }



    @Column(name = "starttime", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }


    @Column(name = "endtime", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }


    @Column(name = "showcount", nullable = true)
    public Integer getShowcount() {
        return showcount;
    }

    public void setShowcount(Integer showcount) {
        this.showcount = showcount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    @Column(name = "title", nullable = true, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Column(name = "page", nullable = true, length = 20)
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }




    @Column(name = "ishot", nullable = true)
    public Boolean getIshot() {
        return ishot;
    }

    public void setIshot(Boolean ishot) {
        this.ishot = ishot;
    }
}
