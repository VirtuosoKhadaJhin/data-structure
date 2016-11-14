package com.nuanyou.cms.entity.order;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.user.PasUserProfile;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Felix on 2016/9/9.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_user_tel")
public class UserTel {
    private Long id;
    private PasUserProfile user;
    private String tel;
    private Date createtime;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    public PasUserProfile getUser() {
        return user;
    }

    public void setUser(PasUserProfile user) {
        this.user = user;
    }


    @Column(name = "tel", nullable = true, length = 100)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


}
