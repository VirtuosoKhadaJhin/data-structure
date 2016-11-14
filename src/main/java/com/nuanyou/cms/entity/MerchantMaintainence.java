package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_merchant_maintainence")
public class MerchantMaintainence {
    private Long id;
    private Long mchid;
    private Date mcreatetime;
    private Date mupdatetime;
    private String mfootnote;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "mchid", nullable = false)
    public Long getMchid() {
        return mchid;
    }

    public void setMchid(Long mchid) {
        this.mchid = mchid;
    }


    @Column(name = "mcreatetime", nullable = false)
    public Date getMcreatetime() {
        return mcreatetime;
    }

    public void setMcreatetime(Date mcreatetime) {
        this.mcreatetime = mcreatetime;
    }


    @LastModified
    @Column(name = "mupdatetime", nullable = true)
    public Date getMupdatetime() {
        return mupdatetime;
    }

    public void setMupdatetime(Date mupdatetime) {
        this.mupdatetime = mupdatetime;
    }


    @Column(name = "mfootnote", nullable = true, length = 250)
    public String getMfootnote() {
        return mfootnote;
    }

    public void setMfootnote(String mfootnote) {
        this.mfootnote = mfootnote;
    }

}
