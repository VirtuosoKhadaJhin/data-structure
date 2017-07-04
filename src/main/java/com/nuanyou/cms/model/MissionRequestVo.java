package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Byron on 2017/6/27.
 */
public class MissionRequestVo {

    private Long mchId;

    private Long bdId;

    private MissionTaskStatus status = MissionTaskStatus.FINISHED;

    private String remark;

    private Long groupId;

    private Long country = 1L;

    private Long city;

    private Long district;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishDt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date distrDt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditDt;

    private Boolean isAudit = true;

    private int index = 1;

    private int pageSize = 20;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getDistrict() {
        return district;
    }

    public void setDistrict(Long district) {
        this.district = district;
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

    public Date getAuditDt() {
        return auditDt;
    }

    public void setAuditDt(Date auditDt) {
        this.auditDt = auditDt;
    }

    public Boolean getAudit() {
        return isAudit;
    }

    public void setAudit(Boolean audit) {
        isAudit = audit;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getFinishDtStr() {
        if (finishDt == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(finishDt);
    }

    public String getAuditDtStr(){
        if (auditDt == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(auditDt);
    }
}
