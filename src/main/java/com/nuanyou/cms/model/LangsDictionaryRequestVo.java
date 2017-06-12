package com.nuanyou.cms.model;

import java.io.Serializable;

/**
 * Created by nuanyoupc7 on 2017/6/9.
 */
public class LangsDictionaryRequestVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer index = 1;

    private Integer pageNum = 20;

    private String baseNameStr;

    private String keyCode;

    private String message;

    private Integer langsKey;

    public LangsDictionaryRequestVo() {
    }

    public LangsDictionaryRequestVo(Integer index, Integer pageNum) {
        this.index = index;
        this.pageNum = pageNum;
    }

    public LangsDictionaryRequestVo(Integer index, Integer pageNum, String baseNameStr, String keyCode, String message) {
        this.index = index;
        this.pageNum = pageNum;
        this.baseNameStr = baseNameStr;
        this.keyCode = keyCode;
        this.message = message;
    }

    public LangsDictionaryRequestVo(Integer index, Integer pageNum, String baseNameStr, String keyCode, String message, Integer langsKey) {
        this.index = index;
        this.pageNum = pageNum;
        this.baseNameStr = baseNameStr;
        this.keyCode = keyCode;
        this.message = message;
        this.langsKey = langsKey;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getBaseNameStr() {
        return baseNameStr;
    }

    public void setBaseNameStr(String baseNameStr) {
        this.baseNameStr = baseNameStr;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLangsKey() {
        return langsKey;
    }

    public void setLangsKey(Integer langsKey) {
        this.langsKey = langsKey;
    }
}
