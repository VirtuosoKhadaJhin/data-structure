package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import com.nuanyou.cms.entity.mission.BdMerchantTrack;

import java.util.Date;

/**
 * Created by Byron on 2017/6/27.
 */
public class MissionTaskVo {

    private Long id;

    private Long mchId;

    private String mchName;

    private Long groupId;

    private Long bdId;

    private MissionTaskStatus status;

    private String version;

    private String orderno;

    private String remark;

    private Long auditor;

    private Date auditdt;

    private Date finishdt;

    private Date createDt;

    private Date updateDt;

    private Boolean delFlag = false;

    private BdMerchantTrack merchantTrack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getBdId() {
        return bdId;
    }

    public void setBdId(Long bdId) {
        this.bdId = bdId;
    }

    public MissionTaskStatus getStatus() {
        return status;
    }

    public void setStatus(MissionTaskStatus status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public Date getAuditdt() {
        return auditdt;
    }

    public void setAuditdt(Date auditdt) {
        this.auditdt = auditdt;
    }

    public Date getFinishdt() {
        return finishdt;
    }

    public void setFinishdt(Date finishdt) {
        this.finishdt = finishdt;
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

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public BdMerchantTrack getMerchantTrack() {
        return merchantTrack;
    }

    public void setMerchantTrack(BdMerchantTrack merchantTrack) {
        this.merchantTrack = merchantTrack;
    }
}
