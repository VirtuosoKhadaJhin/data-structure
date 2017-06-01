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

    private int index;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LangsCategory)) return false;

        LangsCategory category = (LangsCategory) o;

        if (getIndex () != category.getIndex ()) return false;
        if (getId () != null ? !getId ().equals ( category.getId () ) : category.getId () != null) return false;
        if (getName () != null ? !getName ().equals ( category.getName () ) : category.getName () != null) return false;
        if (isGlobal != null ? !isGlobal.equals ( category.isGlobal ) : category.isGlobal != null) return false;
        if (getDesc () != null ? !getDesc ().equals ( category.getDesc () ) : category.getDesc () != null) return false;
        if (getCreateDt () != null ? !getCreateDt ().equals ( category.getCreateDt () ) : category.getCreateDt () != null)
            return false;
        if (getUpdateDt () != null ? !getUpdateDt ().equals ( category.getUpdateDt () ) : category.getUpdateDt () != null)
            return false;
        if (getUserId () != null ? !getUserId ().equals ( category.getUserId () ) : category.getUserId () != null)
            return false;
        return getDelFlag () != null ? getDelFlag ().equals ( category.getDelFlag () ) : category.getDelFlag () == null;
    }

    @Override
    public int hashCode() {
        int result = getId () != null ? getId ().hashCode () : 0;
        result = 31 * result + (getName () != null ? getName ().hashCode () : 0);
        result = 31 * result + (isGlobal != null ? isGlobal.hashCode () : 0);
        result = 31 * result + (getDesc () != null ? getDesc ().hashCode () : 0);
        result = 31 * result + (getCreateDt () != null ? getCreateDt ().hashCode () : 0);
        result = 31 * result + (getUpdateDt () != null ? getUpdateDt ().hashCode () : 0);
        result = 31 * result + (getUserId () != null ? getUserId ().hashCode () : 0);
        result = 31 * result + (getDelFlag () != null ? getDelFlag ().hashCode () : 0);
        result = 31 * result + getIndex ();
        return result;
    }

    @Override
    public String toString() {
        return "LangsCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isGlobal=" + isGlobal +
                ", desc='" + desc + '\'' +
                ", createDt=" + createDt +
                ", updateDt=" + updateDt +
                ", userId=" + userId +
                ", delFlag=" + delFlag +
                ", index=" + index +
                '}';
    }
}
