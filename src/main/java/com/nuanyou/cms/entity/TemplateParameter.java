package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bd_contract_template_parameter",catalog = "bd")
@EntityListeners(DateEntityListener.class)
public class TemplateParameter {
    private Long id;
    private Long templateid;
    private String name;
    private String zhcn;
    private String jajp;
    private String kokr;
    private String th;
    private String dede;
    private String key;
    private String remark;
    private String defaultValue;
    private Byte type;
    private Integer sort;
    private Date updateTime;
    private Date createTime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "templateid")
    public Long getTemplateid() {
        return templateid;
    }

    public void setTemplateid(Long templateid) {
        this.templateid = templateid;
    }

    @Column(name = "`name`")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "`key`")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @LastModified
    @Column(name = "updatetime", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "defaultvalue")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "type")
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Column(name = "sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "zhcn")
    public String getZhcn() {
        return zhcn;
    }

    public void setZhcn(String zhcn) {
        this.zhcn = zhcn;
    }

    @Column(name = "jajp")
    public String getJajp() {
        return jajp;
    }

    public void setJajp(String jajp) {
        this.jajp = jajp;
    }

    @Column(name = "kokr")
    public String getKokr() {
        return kokr;
    }

    public void setKokr(String kokr) {
        this.kokr = kokr;
    }

    @Column(name = "th")
    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    @Column(name = "dede")
    public String getDede() {
        return dede;
    }

    public void setDede(String dede) {
        this.dede = dede;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
