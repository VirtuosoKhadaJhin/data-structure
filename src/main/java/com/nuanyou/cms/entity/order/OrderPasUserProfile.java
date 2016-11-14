package com.nuanyou.cms.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Felix on 2016/9/9.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "pas_user_profile", schema = "passport", catalog = "")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler", "userTel"})
public class OrderPasUserProfile implements Serializable {
    private Long id;
    private String nickname;
    private String mobile;
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








}
