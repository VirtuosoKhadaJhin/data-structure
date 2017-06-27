package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_district")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class District {
    private Long id;
    private String name;
    private String shortname;
    private String kpname;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "name", nullable = true, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "kpname", nullable = true, length = 30)
    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }

    @Column(name = "enname", nullable = true, length = 30)
    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    @Column(name = "startlongitude", nullable = true, precision = 0)
    public Double getStartlongitude() {
        return startlongitude;
    }

    public void setStartlongitude(Double startlongitude) {
        this.startlongitude = startlongitude;
    }

    @Column(name = "endlongitude", nullable = true, precision = 0)
    public Double getEndlongitude() {
        return endlongitude;
    }

    public void setEndlongitude(Double endlongitude) {
        this.endlongitude = endlongitude;
    }


    @Column(name = "startlatitude", nullable = true, precision = 0)
    public Double getStartlatitude() {
        return startlatitude;
    }

    public void setStartlatitude(Double startlatitude) {
        this.startlatitude = startlatitude;
    }


    @Column(name = "endlatitude", nullable = true, precision = 0)
    public Double getEndlatitude() {
        return endlatitude;
    }

    public void setEndlatitude(Double endlatitude) {
        this.endlatitude = endlatitude;
    }


    @Column(name = "indeximgurl", nullable = true, length = 200)
    public String getIndeximgurl() {
        return indeximgurl;
    }

    public void setIndeximgurl(String indeximgurl) {
        this.indeximgurl = indeximgurl;
    }


    @Column(name = "bgimgurl", nullable = true, length = 200)
    public String getBgimgurl() {
        return bgimgurl;
    }

    public void setBgimgurl(String bgimgurl) {
        this.bgimgurl = bgimgurl;
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


    @Column(name = "link", nullable = true, length = 500)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    @Column(name = "icon", nullable = true, length = 500)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
    @JoinColumn(name = "cityid")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    @ManyToOne
    @JoinColumn(name = "countryid")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    @Column(name = "radio", nullable = true, precision = 2)
    public BigDecimal getRadio() {
        return radio;
    }

    @Column(name = "shortname")
    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public void setRadio(BigDecimal radio) {
        this.radio = radio;
    }


    public District(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public District(Country country) {
        this.country = country;
    }

    public District() {
    }

}
