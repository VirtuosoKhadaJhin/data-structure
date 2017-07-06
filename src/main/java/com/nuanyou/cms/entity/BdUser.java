package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bd_user") //catalog配置访问的数据库
@EntityListeners(DateEntityListener.class)
public class BdUser {
    private Long id;
    private String name;
    private String chineseName;
    private String pwd;
    private Country country;
    private String email;
    private String dmail;
    private Byte deleted;
    private Date createTime;
    private Date updateTime;

    public BdUser() {
    }

    public BdUser(String dmail) {
        this.dmail = dmail;
    }

    public BdUser(Long id) {
        this.id = id;
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

    @Column(name = "chinesename")
    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    @Column(name = "pwd")
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "countryid")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "dmail")
    public String getDmail() {
        return dmail;
    }

    public void setDmail(String dmail) {
        this.dmail = dmail;
    }

    @Column(name = "deleted")
    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    @CreatedAt //自动添加创建时间
    @Column(name = "createtime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @LastModified //自动更新时间
    @Column(name = "updatetime")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}