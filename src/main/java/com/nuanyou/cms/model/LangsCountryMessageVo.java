package com.nuanyou.cms.model;

import java.io.Serializable;

/**
 * Created by Byron on 2017/6/6.
 */
public class LangsCountryMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 用于修改的ID
    private Long id;

    //LangsCountry.key
    private Integer langsKey;

    //内容
    private String message;

    public LangsCountryMessageVo(Integer langsKey, String message) {
        this.langsKey = langsKey;
        this.message = message;
    }

    public LangsCountryMessageVo(Integer langsKey, String message, String language, String country) {
        this.langsKey = langsKey;
        this.message = message;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
