package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.Sex;
import com.nuanyou.cms.entity.enums.SexConverter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Felix on 2016/9/27.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_fake_user")
public class FakeUser {

    private Long id;
    private String nickname;
    private Sex sex;
    private String headImgUrl;
    private Date createTime;

    public FakeUser() {
    }

    public FakeUser(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "nickname", nullable = true, length = 40)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "sex", nullable = true)
    @Convert(converter = SexConverter.class)
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }


    @Column(name = "headimgurl", nullable = true, length = 255)
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



}
