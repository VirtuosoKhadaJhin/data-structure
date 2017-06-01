package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Byron on 2017/5/26.
 */
public class LangsDictionary implements Serializable{

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
     * 字典項类型
     */
    @ApiModelProperty(value = "字典項类型")
    @JsonProperty("baseName")
    private String baseName;

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

    private Integer index;

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

    @Override
    public String toString() {
        return "LangsDictionary{" +
                "id=" + id +
                ", keyCode='" + keyCode + '\'' +
                ", baseName='" + baseName + '\'' +
                ", message='" + message + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", variant='" + variant + '\'' +
                ", createDt=" + createDt +
                ", updateDt=" + updateDt +
                ", userId=" + userId +
                ", delFlag=" + delFlag +
                ", index=" + index +
                '}';
    }
}
