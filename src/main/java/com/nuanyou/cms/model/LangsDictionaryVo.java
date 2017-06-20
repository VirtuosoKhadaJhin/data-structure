package com.nuanyou.cms.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Byron on 2017/6/6.
 */
public class LangsDictionaryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //key+category
    private String keyCode;

    //修改后新值
    private String newKeyCode;

    // 接收当地语言
    private String localMess;

    // 语言的key
    private Integer langsKey;

    //分类ID
    private Long categoryId;

    //分类名称
    private String categoryName;

    //分类名称
    private Date updateDt;

    // 接收备注信息
    private LangsMessageTipVo messageTip;

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

    public String getNewKeyCode() {
        return newKeyCode;
    }

    public void setNewKeyCode(String newKeyCode) {
        this.newKeyCode = newKeyCode;
    }

    public String getLocalMess() {
        return localMess;
    }

    public void setLocalMess(String localMess) {
        this.localMess = localMess;
    }

    public Integer getLangsKey() {
        return langsKey;
    }

    public void setLangsKey(Integer langsKey) {
        this.langsKey = langsKey;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LangsMessageTipVo getMessageTip() {
        return messageTip;
    }

    public void setMessageTip(LangsMessageTipVo messageTip) {
        this.messageTip = messageTip;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public List<LangsCountryMessageVo> getLangsMessageList() {
        return langsMessageList;
    }

    public void setLangsMessageList(List<LangsCountryMessageVo> langsMessageList) {
        this.langsMessageList = langsMessageList;
    }


}
