package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 战队
 * Created by sharp on 2017/6/28 - 15:01
 */
@Entity
@EntityListeners(DateEntityListener.class) //自动更新时间
@Table(name = "ny_mission_group", catalog = "nuanyou20") //catalog配置访问的数据库
public class MissionGroup {
    
    private Long id;
    private String name;
    private Country country;
    private City city;
    private Byte isPublic;
    private String desc;
    private Date createDt;
    private Date updateDt;
    private Byte delFlag = 0;
    private BdUser leaderId;
    
    public MissionGroup() {
    }
    
    public MissionGroup(String name, Country country, City city) {
        this.name = name;
        this.country = country;
        this.city = city;
    }
    
    public MissionGroup(String name, Country country, City city, Byte isPublic, String desc, BdUser leaderId) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.isPublic = isPublic;
        this.desc = desc;
        this.leaderId = leaderId;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name = "name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid")
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityid")
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
    
    @Column(name = "ispublic")
    public Byte getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Byte isPublic) {
        this.isPublic = isPublic;
    }
    
    @Column(name = "`desc`")
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    @CreatedAt //自动添加创建时间
    @Column(name = "createdt")
    public Date getCreateDt() {
        return createDt;
    }
    
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
    
    @LastModified //自动更新时间
    @Column(name = "updatedt")
    public Date getUpdateDt() {
        return updateDt;
    }
    
    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }
    
    @Column(name = "delflag")
    public Byte getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderid")
    public BdUser getLeaderId() {
        return leaderId;
    }
    
    public void setLeaderId(BdUser leaderId) {
        this.leaderId = leaderId;
    }
}
