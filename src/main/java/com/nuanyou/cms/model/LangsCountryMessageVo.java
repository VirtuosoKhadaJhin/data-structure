package com.nuanyou.cms.model;

import java.io.Serializable;

/**
 * Created by Byron on 2017/6/6.
 */
public class LangsCountryMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //LangsCountry.key
    private Integer langsKey;

    //内容
    private String message;

    private String language;

    private String country;

    public LangsCountryMessageVo(Integer langsKey, String message) {
        this.langsKey = langsKey;
        this.message = message;
    }

    public LangsCountryMessageVo(Integer langsKey, String message, String language, String country) {
        this.langsKey = langsKey;
        this.message = message;
        this.language = language;
        this.country = country;
    }

    public Integer getLangsKey() {
        return langsKey;
    }

    public void setLangsKey(Integer langsKey) {
        this.langsKey = langsKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LangsCountryMessageVo() {
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

}
