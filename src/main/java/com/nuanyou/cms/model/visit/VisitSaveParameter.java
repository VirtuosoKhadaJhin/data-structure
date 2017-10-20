package com.nuanyou.cms.model.visit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by young on 2017/9/18.
 */
public class VisitSaveParameter {

    private Long merchantId;
    private  Long userId;
    private String name;
    private Long catId;
    private Long subCatId;
    private Long districtId;
    private String tels;
    private String email;
    private Double longitude;
    private Double latitude;
    private String indexImgUrl;
    private String headImgUrls;
    private BigDecimal consumptionPerPerson;
    private Date openingStartTime;
    private Date openingEndTime;
    private String address;
    private String businessDays;
    private String payTypes;
    private String supportTypes;
    private  boolean locateExactly;

    private String note;
    private String signImgUrls;
    private String collectionCodes;

    private Integer cooperationStatus;
    private String contactPerson;
    private String contactInfo;
    private String tag;
    private Integer visitType;
    @JsonProperty("merchantid")
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
    @JsonProperty("userid")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("catid")
    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }
    @JsonProperty("subcatid")
    public Long getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(Long subCatId) {
        this.subCatId = subCatId;
    }
    @JsonProperty("districtid")
    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
    @JsonProperty("tels")
    public String getTels() {
        return tels;
    }

    public void setTels(String tels) {
        this.tels = tels;
    }
    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    @JsonProperty("indeximgurl")
    public String getIndexImgUrl() {
        return indexImgUrl;
    }

    public void setIndexImgUrl(String indexImgUrl) {
        this.indexImgUrl = indexImgUrl;
    }
    @JsonProperty("headimgurls")
    public String getHeadImgUrls() {
        return headImgUrls;
    }

    public void setHeadImgUrls(String headImgUrls) {
        this.headImgUrls = headImgUrls;
    }
    @JsonProperty("consumptionperperson")
    public BigDecimal getConsumptionPerPerson() {
        return consumptionPerPerson;
    }

    public void setConsumptionPerPerson(BigDecimal consumptionPerPerson) {
        this.consumptionPerPerson = consumptionPerPerson;
    }
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @JsonProperty("openingstarttime")
    public Date getOpeningStartTime() {
        return openingStartTime;
    }

    public void setOpeningStartTime(Date openingStartTime) {
        this.openingStartTime = openingStartTime;
    }
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @JsonProperty("openingendtime")
    public Date getOpeningEndTime() {
        return openingEndTime;
    }

    public void setOpeningEndTime(Date openingEndTime) {
        this.openingEndTime = openingEndTime;
    }
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @JsonProperty("businessdays")
    public String getBusinessDays() {
        return businessDays;
    }

    public void setBusinessDays(String businessDays) {
        this.businessDays = businessDays;
    }
    @JsonProperty("paytypes")
    public String getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(String payTypes) {
        this.payTypes = payTypes;
    }
    @JsonProperty("supporttypes")
    public String getSupportTypes() {
        return supportTypes;
    }

    public void setSupportTypes(String supportTypes) {
        this.supportTypes = supportTypes;
    }
    @JsonProperty("locateexactly")
    public boolean isLocateExactly() {
        return locateExactly;
    }

    public void setLocateExactly(boolean locateExactly) {
        this.locateExactly = locateExactly;
    }
    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    @JsonProperty("signimgurls")
    public String getSignImgUrls() {
        return signImgUrls;
    }

    public void setSignImgUrls(String signImgUrls) {
        this.signImgUrls = signImgUrls;
    }
    @JsonProperty("collectioncodes")
    public String getCollectionCodes() {
        return collectionCodes;
    }

    public void setCollectionCodes(String collectionCodes) {
        this.collectionCodes = collectionCodes;
    }
    @JsonProperty("cooperationstatus")
    public Integer getCooperationStatus() {
        return cooperationStatus;
    }

    public void setCooperationStatus(Integer cooperationStatus) {
        this.cooperationStatus = cooperationStatus;
    }
    @JsonProperty("contactperson")
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    @JsonProperty("contactinfo")
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    @JsonProperty("visittype")
    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
