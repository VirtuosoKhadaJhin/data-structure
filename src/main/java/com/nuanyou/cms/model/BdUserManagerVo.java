package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.Country;

/**
 * // TODO: 2017/6/22 注释
 * Created by sharp on 2017/6/22 - 14:53
 */
public class BdUserManagerVo {
    private BdUser user;
    private BdRole role; //角色
    private Country country;
    
    
    public BdUserManagerVo() {
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public BdUser getUser() {
        return user;
    }
    
    public void setUser(BdUser user) {
        this.user = user;
    }
    
    
    
    public BdRole getRole() {
        return role;
    }
    
    public void setRole(BdRole role) {
        this.role = role;
    }
}
