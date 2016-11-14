package com.nuanyou.cms.model;

import com.nuanyou.cms.commons.LastModified;
import com.nuanyou.cms.entity.City;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Felix on 2016/9/30.
 */
public class RankVO {

    private Long id;
    private City city;
    private Integer objid;
    private Byte objtype;
    private Integer sort;
    private Boolean display;
    private Date createtime;
    private Date updatetime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityid")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    @Column(name = "objid", nullable = true)
    public Integer getObjid() {
        return objid;
    }

    public void setObjid(Integer objid) {
        this.objid = objid;
    }


    @Column(name = "objtype", nullable = true)
    public Byte getObjtype() {
        return objtype;
    }

    public void setObjtype(Byte objtype) {
        this.objtype = objtype;
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


    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }


    @LastModified
    @Column(name = "updatetime", nullable = true)
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
