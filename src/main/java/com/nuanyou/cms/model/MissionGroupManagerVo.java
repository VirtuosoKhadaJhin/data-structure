package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;

import java.util.Date;

/**
 * 在视图显示的实体
 * Created by sharp on 2017/6/28 - 15:58
 */
public class MissionGroupManagerVo {
    private Long id;
    private String name;
    private Country country;
    private City city;
    private Byte isPublic;
    private String desc;
    private Date createDt;
    private Date updateDt;
    private Byte delFlag;
    private BdUser leaderId;
    Integer index = 1;
    Integer pageNum = 8;
    
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
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
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
    
    public BdUser getLeaderId() {
        return leaderId;
    }
    
    public void setLeaderId(BdUser leaderId) {
        this.leaderId = leaderId;
    }
    
    public Integer getIndex() {
        return index;
    }
    
    public void setIndex(Integer index) {
        this.index = index;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
