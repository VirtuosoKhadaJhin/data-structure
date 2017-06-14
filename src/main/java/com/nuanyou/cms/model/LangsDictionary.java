package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nuanyou.cms.model.enums.LangsCountry;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Byron on 2017/5/26.
 */
public class LangsDictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典项ID
     */
    @ApiModelProperty(value = "字典项ID")
    @JsonProperty("id")
    private Long id;

    /**
     * 字典项KeyCode
     */
    @ApiModelProperty(value = "字典项KeyCode")
    @JsonProperty("keyCode")
    private String keyCode;

    /**
     * 字典項类型ID
     */
    @ApiModelProperty(value = "字典項类型ID")
    @JsonProperty("baseName")
    private Long baseName;

    /**
     * 多元化内容
     */
    @ApiModelProperty(value = "多元化内容")
    @JsonProperty("message")
    private String message;

    /**
     * 语言代码
     */
    @ApiModelProperty(value = "语言代码")
    @JsonProperty("language")
    private String language;

    /**
     * 国家代码
     */
    @ApiModelProperty(value = "国家代码")
    @JsonProperty("country")
    private String country;

    /**
     * Locale变量
     */
    @ApiModelProperty(value = "Locale变量")
    @JsonProperty("variant")
    private String variant;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("createDt")
    private Date createDt;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonProperty("updateDt")
    private Date updateDt;

    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    @JsonProperty("userId")
    private Long userId;

    /**
     * 刪除标记位
     */
    @ApiModelProperty(value = "刪除标记位")
    @JsonProperty("delFlag")
    private Boolean delFlag = false;

    private Integer index = 1;

    private Integer size = 20;

    private String baseNameStr;//UI查询使用

    private Integer langsCountryKey;//UI查询使用

    private LangsMessageTipVo messageTip;

    public LangsDictionary() {
    }

    public LangsDictionary(String keyCode, String variant) {
        this.keyCode = keyCode;
        this.variant = variant;
    }

    public LangsDictionary(Integer langsCountryKey, String language, String country) {
        this.langsCountryKey = langsCountryKey;
        this.language = language;
        this.country = country;
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

    public Long getBaseName() {
        return baseName;
    }

    public void setBaseName(Long baseName) {
        this.baseName = baseName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getBaseNameStr() {
        return baseNameStr;
    }

    public void setBaseNameStr(String baseNameStr) {
        this.baseNameStr = baseNameStr;
    }

    public LangsMessageTipVo getMessageTip() {
        return messageTip;
    }

    public void setMessageTip(LangsMessageTipVo messageTip) {
        this.messageTip = messageTip;
    }

    public String getLangsCountry() {
        return this.getLanguage() + "-" + this.getCountry();
    }

    public String getLangsCountryDesc() {
        return LangsCountry.toEnum(this.getLanguage() + "-" + this.getCountry()).getDesc();
    }

    public Integer getLangsCountryKey() {
        return langsCountryKey;
    }

    public void setLangsCountryKey(Integer langsCountryKey) {
        this.langsCountryKey = langsCountryKey;
    }
}
