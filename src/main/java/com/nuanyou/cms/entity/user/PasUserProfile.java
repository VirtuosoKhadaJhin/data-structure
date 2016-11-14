package com.nuanyou.cms.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.Sex;
import com.nuanyou.cms.entity.enums.SexConverter;
import com.nuanyou.cms.entity.order.UserTel;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Felix on 2016/9/9.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "pas_user_profile", schema = "passport", catalog = "")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler", "userTel"})
public class PasUserProfile implements Serializable {
    private Long id;
    private Date birthday;
    private String city;
    private String country;
    private Date createtime;
    private String email;
    private String headimgurl;
    private String mobile;
    private String nickname;
    private String province;
    private String realname;
    private Sex sex;
    private String sceneid;
    private String tempsceneid;
    private Date tempscenetime;
    private String unionid;
    private BigDecimal balance;
    private BigDecimal frozenbalance;
    private Date scenetime;

    private Long userid;
    private UserTel userTel;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    public UserTel getUserTel() {
        return userTel;
    }

    public void setUserTel(UserTel userTel) {
        this.userTel = userTel;
    }


    @Column(name = "userid", nullable = false)
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    @Column(name = "birthday", nullable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    @Column(name = "city", nullable = true, length = 30)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Column(name = "country", nullable = true, length = 30)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    @Column(name = "email", nullable = true, length = 60)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Column(name = "headimgurl", nullable = true, length = 255)
    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }


    @Column(name = "mobile", nullable = true, length = 20)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Column(name = "nickname", nullable = true, length = 50)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    @Column(name = "province", nullable = true, length = 30)
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    @Column(name = "realname", nullable = true, length = 50)
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }


    @Convert(converter = SexConverter.class)
    @Column(name = "sex", nullable = true)
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }


    @Column(name = "sceneid", nullable = true, length = 64)
    public String getSceneid() {
        return sceneid;
    }

    public void setSceneid(String sceneid) {
        this.sceneid = sceneid;
    }


    @Column(name = "tempsceneid", nullable = true, length = 64)
    public String getTempsceneid() {
        return tempsceneid;
    }

    public void setTempsceneid(String tempsceneid) {
        this.tempsceneid = tempsceneid;
    }


    @Column(name = "tempscenetime", nullable = true)
    public Date getTempscenetime() {
        return tempscenetime;
    }

    public void setTempscenetime(Date tempscenetime) {
        this.tempscenetime = tempscenetime;
    }


    @Column(name = "unionid", nullable = true, length = 255)
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }


    @Column(name = "balance", nullable = true, precision = 2)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    @Column(name = "frozenbalance", nullable = true, precision = 2)
    public BigDecimal getFrozenbalance() {
        return frozenbalance;
    }

    public void setFrozenbalance(BigDecimal frozenbalance) {
        this.frozenbalance = frozenbalance;
    }


    @Column(name = "scenetime", nullable = true)
    public Date getScenetime() {
        return scenetime;
    }

    public void setScenetime(Date scenetime) {
        this.scenetime = scenetime;
    }

}
