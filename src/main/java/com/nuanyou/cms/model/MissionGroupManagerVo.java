package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.entity.MissionGroupBd;

/**
 * 在视图显示的实体
 * Created by sharp on 2017/6/28 - 15:58
 */
public class MissionGroupManagerVo {
    Integer index = 1;
    Integer pageNum = 8;
    
    private MissionGroup group;
    private MissionGroupBd groupBd;
    private Country country;
    private City city;
    
    public MissionGroupManagerVo() {
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
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
    
    public MissionGroup getGroup() {
        return group;
    }
    
    public void setGroup(MissionGroup group) {
        this.group = group;
    }
    
    public MissionGroupBd getGroupBd() {
        return groupBd;
    }
    
    public void setGroupBd(MissionGroupBd groupBd) {
        this.groupBd = groupBd;
    }
}
