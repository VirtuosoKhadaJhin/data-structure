package com.nuanyou.cms.entity;

/**
 * Created by Byron on 2017/6/12.
 */

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ny_langs_message")
@EntityListeners({AuditingEntityListener.class})
public class EntityNyLangsMessageTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "KEY_CODE")
    private String keyCode;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "IMG_URL")
    private String imgUrl;

    @Column(name = "CREATE_DT")
    private Date createDt;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "DEL_FLAG")
    private Boolean delFlag = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }
}
