package com.nuanyou.cms.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Byron on 2017/6/6.
 */
public class LangsDictionaryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //key+category
    private String keyCode;

    //分类ID
    private Long categoryId;

    //国家语言+内容
    private List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();

    public LangsDictionaryVo() {
    }

    public LangsDictionaryVo(String keyCode, Long categoryId) {
        this.keyCode = keyCode;
        this.categoryId = categoryId;
    }

    public LangsDictionaryVo(String keyCode) {
        this.keyCode = keyCode;
    }

    public LangsDictionaryVo(String keyCode, Long categoryId, List<LangsCountryMessageVo> langsMessageList) {
        this.keyCode = keyCode;
        this.categoryId = categoryId;
        this.langsMessageList = langsMessageList;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<LangsCountryMessageVo> getLangsMessageList() {
        return langsMessageList;
    }

    public void setLangsMessageList(List<LangsCountryMessageVo> langsMessageList) {
        this.langsMessageList = langsMessageList;
    }


}
