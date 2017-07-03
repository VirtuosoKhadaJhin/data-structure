package com.nuanyou.cms.model;

/**
 * Created by sharp on 2017/6/28 - 21:25
 */
public class MissionGroupParamVo {
    private String name;
    private Long countryId;
    private Long cityId;
    private String isPublic;
    private String desc;

    private Long leaderId;
    private Long viceLeaderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Long getViceLeaderId() {
        return viceLeaderId;
    }

    public void setViceLeaderId(Long viceLeaderId) {
        this.viceLeaderId = viceLeaderId;
    }
}
