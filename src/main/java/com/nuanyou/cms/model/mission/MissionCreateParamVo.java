package com.nuanyou.cms.model.mission;

/**
 * Created by young on 2017/9/4.
 */
public class MissionCreateParamVo {

    private Long mchId;

    private Long groupId;

    private String version;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getVersion() {
        return version;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
