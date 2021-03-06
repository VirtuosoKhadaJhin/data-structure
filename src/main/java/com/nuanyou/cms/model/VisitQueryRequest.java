package com.nuanyou.cms.model;

import org.apache.catalina.LifecycleState;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/9/1.
 */
public class VisitQueryRequest {

    private Long userId;

    private Long countryId;

    private Long cityId;

    private Long mchId;

    private Long districtId;

    private Long visitTypeId;

    private String countryids;

    private String mchName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private int index = 1;

    private int pageSize = 20;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeStr() {
        if (startTime == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(startTime);
    }
    public String getEndTimeStr() {
        if (endTime == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(endTime);
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(Long visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public String getCountryids() {
        return countryids;
    }

    public void setCountryids(String countryids) {
        this.countryids = countryids;
    }
}
