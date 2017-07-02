package com.nuanyou.cms.model;

/**
 * 添加bduser
 * Created by sharp on 2017/6/29 - 11:21
 */
public class BdUserParamVo {
    private Long id;
    private String name;
    private String chineseName;
    private Long countryId;
    private String email;
    private String dmail;
    private Long roleId;
    
    public BdUserParamVo() {
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
    
    public String getChineseName() {
        return chineseName;
    }
    
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
    
    public Long getCountryId() {
        return countryId;
    }
    
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDmail() {
        return dmail;
    }
    
    public void setDmail(String dmail) {
        this.dmail = dmail;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
