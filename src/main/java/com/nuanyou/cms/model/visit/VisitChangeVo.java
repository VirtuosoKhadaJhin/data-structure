package com.nuanyou.cms.model.visit;

import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/9/18.
 */
public class VisitChangeVo {

    private VisitDetailExtension<String> localName;

    private VisitDetailExtension<String> districtName;

    private VisitDetailExtension<String> localAddress;

    private VisitDetailExtension<String> cooperationName;

    private VisitDetailExtension<String> catName;

    private VisitDetailExtension<String> subCatName;

    private VisitDetailExtension<Date> startTime;

    private VisitDetailExtension<Date> endTime;

    private VisitDetailExtension<List<String>> businessDays;

    private VisitDetailExtension<String> tel;

    private VisitDetailExtension<String> cosume;

    private VisitDetailExtension<List<String>> payTypes;

    private VisitDetailExtension<List<String>> supportTypes;

    private VisitDetailExtension<String> headImg;

    private VisitDetailExtension<String> listImg;

    private VisitDetailExtension<List<String>> detailImgs;

    private VisitDetailExtension<String> tag;

    private Date changeTime;

    private String bdName;

    public VisitDetailExtension<String> getLocalName() {
        return localName;
    }

    public void setLocalName(VisitDetailExtension<String> localName) {
        this.localName = localName;
    }

    public VisitDetailExtension<String> getDistrictName() {
        return districtName;
    }

    public void setDistrictName(VisitDetailExtension<String> districtName) {
        this.districtName = districtName;
    }

    public VisitDetailExtension<String> getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(VisitDetailExtension<String> localAddress) {
        this.localAddress = localAddress;
    }

    public VisitDetailExtension<String> getCooperationName() {
        return cooperationName;
    }

    public void setCooperationName(VisitDetailExtension<String> cooperationName) {
        this.cooperationName = cooperationName;
    }

    public VisitDetailExtension<String> getCatName() {
        return catName;
    }

    public void setCatName(VisitDetailExtension<String> catName) {
        this.catName = catName;
    }

    public VisitDetailExtension<String> getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(VisitDetailExtension<String> subCatName) {
        this.subCatName = subCatName;
    }

    public VisitDetailExtension<Date> getStartTime() {
        return startTime;
    }

    public void setStartTime(VisitDetailExtension<Date> startTime) {
        this.startTime = startTime;
    }

    public VisitDetailExtension<Date> getEndTime() {
        return endTime;
    }

    public void setEndTime(VisitDetailExtension<Date> endTime) {
        this.endTime = endTime;
    }

    public VisitDetailExtension<List<String>> getBusinessDays() {
        return businessDays;
    }

    public void setBusinessDays(VisitDetailExtension<List<String>> businessDays) {
        this.businessDays = businessDays;
    }

    public VisitDetailExtension<String> getTel() {
        return tel;
    }

    public void setTel(VisitDetailExtension<String> tel) {
        this.tel = tel;
    }

    public VisitDetailExtension<String> getCosume() {
        return cosume;
    }

    public void setCosume(VisitDetailExtension<String> cosume) {
        this.cosume = cosume;
    }

    public VisitDetailExtension<List<String>> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(VisitDetailExtension<List<String>> payTypes) {
        this.payTypes = payTypes;
    }

    public VisitDetailExtension<List<String>> getSupportTypes() {
        return supportTypes;
    }

    public void setSupportTypes(VisitDetailExtension<List<String>> supportTypes) {
        this.supportTypes = supportTypes;
    }

    public VisitDetailExtension<String> getHeadImg() {
        return headImg;
    }

    public void setHeadImg(VisitDetailExtension<String> headImg) {
        this.headImg = headImg;
    }

    public VisitDetailExtension<String> getListImg() {
        return listImg;
    }

    public void setListImg(VisitDetailExtension<String> listImg) {
        this.listImg = listImg;
    }

    public VisitDetailExtension<List<String>> getDetailImgs() {
        return detailImgs;
    }

    public void setDetailImgs(VisitDetailExtension<List<String>> detailImgs) {
        this.detailImgs = detailImgs;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getBdName() {
        return bdName;
    }

    public void setBdName(String bdName) {
        this.bdName = bdName;
    }

    public VisitDetailExtension<String> getTag() {
        return tag;
    }

    public void setTag(VisitDetailExtension<String> tag) {
        this.tag = tag;
    }
}
