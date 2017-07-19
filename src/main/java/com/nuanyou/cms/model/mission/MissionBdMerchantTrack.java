package com.nuanyou.cms.model.mission;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by mylon on 2017/7/19.
 */
public class MissionBdMerchantTrack {

    private Long mchId;

    private Long userId;

    private String username;

    private Date createTime;

    private String signImgUrls;

    private int index = 1;

    private int pageSize = 20;

    public MissionBdMerchantTrack() {
    }

    public MissionBdMerchantTrack(Long mchId, Long userId, String username, Date createTime, String signImgUrls, int index, int pageSize) {
        this.mchId = mchId;
        this.userId = userId;
        this.username = username;
        this.createTime = createTime;
        this.signImgUrls = signImgUrls;
        this.index = index;
        this.pageSize = pageSize;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSignImgUrls() {
        return signImgUrls;
    }

    public void setSignImgUrls(String signImgUrls) {
        this.signImgUrls = signImgUrls;
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

    public List<String> getImgUrls(){
        if(StringUtils.isEmpty(signImgUrls)){
            return Lists.newArrayList();
        }
        return Lists.newArrayList(signImgUrls.split(","));
    }

}
