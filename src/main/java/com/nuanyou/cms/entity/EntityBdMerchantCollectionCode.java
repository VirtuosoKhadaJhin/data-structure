package com.nuanyou.cms.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by young on 2017/6/26.
 */
@Entity
@Table(name = "bd_merchant_collectioncode")
@EntityListeners({AuditingEntityListener.class})
public class EntityBdMerchantCollectionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "mchid")
    private Long mchId;
    @Column(name = "mchname")
    private String mchName;
    @Column(name = "countryid")
    private Long countryId;
    @Column(name = "countryname")
    private String countryName;
    @Column(name = "modifierid")
    private Long modifierId;
    @Column(name = "modifier")
    private String modifier;
    @Column(name = "collectioncode")
    private String collectionCode;
    @Column(name = "updatetime")
    private Date updateTime;
    @Column(name = "createtime")
    private Date createTime;
    @Column(name = "isdelete")
    private Boolean isDelete;
    @Column(name = "url")
    private String url;

    @Transient
    private Integer status;//绑定状态  1-已绑定  2-未绑定   空-全部
    @Transient
    private Date startDate;
    @Transient
    private Date endDate;
    @Transient
    private String codes;//收款码集合 以逗号拼接
    @Transient
    private String mchIds;//商户id集合 以逗号拼接


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMchIds() {
        return mchIds;
    }

    public void setMchIds(String mchIds) {
        this.mchIds = mchIds;
    }
}
