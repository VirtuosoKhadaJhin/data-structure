package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.Country;

import java.util.Date;

/**
 * // TODO: 2017/6/22 注释
 * Created by sharp on 2017/6/22 - 14:53
 */
public class BdUserVo {
    private Long id;
    private String name;
    private String pwd;
    private Country country;//所属国家
    private String email;//合同归档邮箱
    private String chineseName;//中文名字
    private String dmail;//钉钉邮箱
    private Byte deleted;
    private Date createTime;
    private Date updateTime;
    private BdRole role; //角色
    
    
    public BdUserVo() {
    }
    
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
    
    public String getPwd() {
        return pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getChineseName() {
        return chineseName;
    }
    
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
    
    public String getDmail() {
        return dmail;
    }
    
    public void setDmail(String dmail) {
        this.dmail = dmail;
    }
    
    public Byte getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public BdRole getRole() {
        return role;
    }
    
    public void setRole(BdRole role) {
        this.role = role;
    }
}
