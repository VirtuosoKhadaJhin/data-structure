package com.nuanyou.cms.model;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/27.
 */
public class CouponBatchVO {

    /**
     * 优惠方案，可多选
     */
    private List<Long> couponTemplateIds;

    /**
     * 商户信息，可多选
     * 格式id:name
     */
    private List<String> mchInfos;

    /**
     * 用户ID，以“;”分隔
     */
    private String userIdText;

    /**
     * 有效期，以月为单位
     */
    private Integer validTime;

    public List<Long> getCouponTemplateIds() {
        return couponTemplateIds;
    }

    public void setCouponTemplateIds(List<Long> couponTemplateIds) {
        this.couponTemplateIds = couponTemplateIds;
    }

    public List<String> getMchInfos() {
        return mchInfos;
    }

    public void setMchInfos(List<String> mchInfos) {
        this.mchInfos = mchInfos;
    }

    public String getUserIdText() {
        return userIdText;
    }

    public void setUserIdText(String userIdText) {
        this.userIdText = userIdText;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

}