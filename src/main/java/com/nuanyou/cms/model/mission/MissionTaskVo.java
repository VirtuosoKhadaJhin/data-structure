package com.nuanyou.cms.model.mission;

import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import com.nuanyou.cms.entity.mission.BdMerchantTrack;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.util.DateUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

/**
 * Created by Byron on 2017/6/27.
 */
public class MissionTaskVo {

    private Long id;

    private Merchant merchant;

    private String mchName;

    private MissionGroup group;

    private BdUser bdUser;

    private MissionTaskStatus status;

    private String version;

    private String mchVersion;

    private String orderno;

    private String remark;

    private Long auditor;

    private Date auditDt;

    private Date finishDt;

    private Date distrDt;

    private Date createDt;

    private Date updateDt;

    private Boolean delFlag = false;

    private BdMerchantTrack merchantTrack = new BdMerchantTrack();

    private Long groupId;

    private Boolean isPublic;

    private Date visitDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public MissionGroup getGroup() {
        return group;
    }

    public void setGroup(MissionGroup group) {
        this.group = group;
    }

    public BdUser getBdUser() {
        return bdUser;
    }

    public void setBdUser(BdUser bdUser) {
        this.bdUser = bdUser;
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

    public String getMchVersion() {
        return mchVersion;
    }

    public void setMchVersion(String mchVersion) {
        this.mchVersion = mchVersion;
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

    public Date getAuditDt() {
        return auditDt;
    }

    public void setAuditDt(Date auditDt) {
        this.auditDt = auditDt;
    }

    public Date getFinishDt() {
        return finishDt;
    }

    public void setFinishDt(Date finishDt) {
        this.finishDt = finishDt;
    }

    public Date getDistrDt() {
        return distrDt;
    }

    public void setDistrDt(Date distrDt) {
        this.distrDt = distrDt;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getVisitDt() {
        return visitDt;
    }

    public void setVisitDt(Date visitDt) {
        this.visitDt = visitDt;
    }

    public boolean getIsExpiredDistributed() {
        if (this.status != MissionTaskStatus.UN_FINISH) {
            return false;
        }
        Pair<Date, Date> startEndTime = DateUtils.getDayStartEndTime(new Date());
        long time = startEndTime.getLeft().getTime();
        if(this.distrDt == null){
            return true;
        }
        long oldTime = this.distrDt.getTime();
        return oldTime < time;
    }
}
