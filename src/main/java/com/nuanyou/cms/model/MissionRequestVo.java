package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.enums.MissionTaskStatus;

import java.util.Date;

/**
 * Created by Byron on 2017/6/27.
 */
public class MissionRequestVo {

    private Long mchId;

    private Long bdId;

    private MissionTaskStatus status = MissionTaskStatus.FINISHED;

    private String remark;

    private Long country = 1L;

    private Long city;

    private Long district;

    private Boolean isAudit = true;

    private Date todayDt;

    private int pageNum = 1;

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

    public Date getTodayDt() {
        return todayDt;
    }

    public void setTodayDt(Date todayDt) {
        this.todayDt = todayDt;
    }

    public Boolean getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(Boolean isAudit) {
        this.isAudit = isAudit;
    }

    public Long getDistrict() {
        return district;
    }

    public void setDistrict(Long district) {
        this.district = district;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}