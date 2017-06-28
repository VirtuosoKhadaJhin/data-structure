package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 战队
 * Created by sharp on 2017/6/28 - 15:01
 */
@Entity
@EntityListeners(DateEntityListener.class) //自动更新时间
@Table(name = "ny_mission_group", catalog = "nuanyou20") //catalog配置访问的数据库
public class MissionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "countryid")
    private Long countryId;
    
    @Column(name = "cityid")
    private Long cityId;
    
    @Column(name = "ispublic")
    private Byte isPublic;
    
    @Column(name = "desc")
    private String  desc;
    
    @CreatedAt //自动添加创建时间
    @Column(name = "createdt")
    private Date createDt;
    
    @LastModified //自动更新时间
    @Column(name = "updatedt")
    private Date updateDt;
    
    @Column(name = "delflag")
    private Byte delFlag;
    
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
    
    public Long getCountryId() {
        return countryId;
    }
    
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
    public Long getCityId() {
        return cityId;
    }
    
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    
    public Byte getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Byte isPublic) {
        this.isPublic = isPublic;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public Date getCreateDt() {
        return createDt;
    }
    
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
    
    public Date getUpdateDt() {
        return updateDt;
    }
    
    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }
    
    public Byte getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }
}
