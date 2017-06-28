package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * bd宝用户
 * <p>
 * Created by sharp on 2017/6/22 - 15:12
 */
@Entity
@Table(name = "bd_user", catalog = "nuanyou20") //catalog配置访问的数据库
@EntityListeners(DateEntityListener.class)
public class BdUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "pwd")
    private String pwd;
    
    @Column(name = "countryid")
    private Long countryId;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "chinesename")
    private String chineseName;
    
    @Column(name = "dmail")
    private String dmail;
    
    @Column(name = "deleted")
    private Byte deleted;
    
    @CreatedAt //自动添加创建时间
    @Column(name = "createtime")
    private Date createTime;
    
    @LastModified //自动更新时间
    @Column(name = "updatetime")
    private Date updateTime;
    
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
}
