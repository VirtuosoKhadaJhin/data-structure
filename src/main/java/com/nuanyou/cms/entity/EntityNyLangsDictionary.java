package com.nuanyou.cms.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Byron on 2017/5/26.
 */
@Entity
@Table(name = "ny_langs_dictionary")
@EntityListeners({AuditingEntityListener.class})
public class EntityNyLangsDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "KEY_CODE")
    private String keyCode;

    @Column(name = "BASE_NAME")
    private String baseName;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "VARIANT")
    private String variant;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "CREATE_DT")
    private Date createDt;

    @Column(name = "UPDATE_DT")
    private Date updateDt;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "DEL_FLAG")
    private Boolean delFlag;

    public EntityNyLangsDictionary() {
    }

    public EntityNyLangsDictionary(String keyCode, String language, String country, String variant) {
        this.keyCode = keyCode;
        this.language = language;
        this.country = country;
        this.variant = variant;
    }

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

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
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
