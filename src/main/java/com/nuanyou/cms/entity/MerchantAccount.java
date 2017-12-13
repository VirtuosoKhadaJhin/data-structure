package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by libin on 2017/12/13.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "sl_merchant_account")
public class MerchantAccount {
    private Integer id;
    private Integer mchid;
    private String password;
    private int activated;
    private String activatedby;
    private int deleted;
    private Date updatetime;
    private Date createtime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "mchid", nullable = true)
    public Integer getMchid() {
        return mchid;
    }

    public void setMchid(Integer mchid) {
        this.mchid = mchid;
    }

    @Column(name = "password", nullable = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "activated", nullable = true)
    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    @Column(name = "activatedby", nullable = true)
    public String getActivatedby() {
        return activatedby;
    }

    public void setActivatedby(String activatedby) {
        this.activatedby = activatedby;
    }
    @Column(name = "deleted", nullable = true)
    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
    @Column(name = "updatetime", nullable = true)
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
