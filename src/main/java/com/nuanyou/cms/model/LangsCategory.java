package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Byron on 2017/5/27.
 */
public class LangsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    @JsonProperty("id")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @JsonProperty("name")
    private String name;

    /**
     * 字典項类型
     */
    @ApiModelProperty(value = "是否全局")
    @JsonProperty("isGlobal")
    private Boolean isGlobal;

    /**
     * 分类描述
     */
    @ApiModelProperty(value = "分类描述")
    @JsonProperty("desc")
    private String desc;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean global) {
        isGlobal = global;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
