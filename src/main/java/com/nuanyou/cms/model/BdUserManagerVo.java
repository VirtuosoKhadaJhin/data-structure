package com.nuanyou.cms.model;

/**
 * // TODO: 2017/6/22 注释
 * Created by sharp on 2017/6/22 - 14:53
 */
public class BdUserManagerVo {
    private Long id;
    private String name;
    private String pwd;
    private String countryName; //所属国家
    private String email; //邮箱
    
    public BdUserManagerVo() {
    }
    
    public BdUserManagerVo(long id) {
        this.id = id;
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
    
    public String getCountryName() {
        return countryName;
    }
    
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
