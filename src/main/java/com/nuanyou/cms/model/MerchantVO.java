package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.entity.MerchantHeadimg;
import com.nuanyou.cms.entity.enums.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public class MerchantVO {
    private Long id;
    private String name;
    private String kpname;
    private String intro;
    private String intro2;
    private String outline;
    private String telphone;
    private Date businessStart;
    private Date businessEnd;
    private List<Week> businessDay;
    private VerifyType verifyType;
    private String address;
    private String kpaddress;
    private Boolean recommend;
    private Double longitude;
    private Double latitude;
    private BigDecimal consume;
    private Integer sort;
    private Boolean display;
    private District district;
    private List<PayType> payTypes;

    //2.0地址 600*200
    private String indexImgUrl;
    //3.0地址 200*150
    private String listImgUrl;

    private List<SupportType> supportType;
    private String tips;
    private Boolean issign;
    private Boolean ispush;
    private Long channelId;
    private Date createTime;
    private Date firstshowTime;
    private Double score;
    private Integer rank;
    private Byte pushType;
    private String code;
    private List<AppointType> appointType;
    private List<MerchantHeadimg> headimgs;

    private Long landmarkId;

    private MerchantCat mcat;
    private MerchantCat subcat;

    private Long catId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String Longro) {
        this.intro = Longro;
    }


    public String getIntro2() {
        return intro2;
    }

    public void setIntro2(String Longro2) {
        this.intro2 = Longro2;
    }


    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }


    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }


    @DateTimeFormat(pattern = "HH:mm:ss", iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    public Date getBusinessStart() {
        return businessStart;
    }

    public void setBusinessStart(Date businessStart) {
        this.businessStart = businessStart;
    }


    @DateTimeFormat(pattern = "HH:mm:ss", iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    public Date getBusinessEnd() {
        return businessEnd;
    }

    public void setBusinessEnd(Date businessEnd) {
        this.businessEnd = businessEnd;
    }


    public List<Week> getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(List<Week> businessDay) {
        this.businessDay = businessDay;
    }


    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getKpaddress() {
        return kpaddress;
    }

    public void setKpaddress(String kpaddress) {
        this.kpaddress = kpaddress;
    }


    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public BigDecimal getConsume() {
        return consume;
    }

    public void setConsume(BigDecimal consume) {
        this.consume = consume;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }


    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }


    public MerchantCat getMcat() {
        return mcat;
    }

    public void setMcat(MerchantCat mcat) {
        this.mcat = mcat;
    }


    public MerchantCat getSubcat() {
        return subcat;
    }

    public void setSubcat(MerchantCat subcat) {
        this.subcat = subcat;
    }


    public List<PayType> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<PayType> payTypes) {
        this.payTypes = payTypes;
    }


    public String getIndexImgUrl() {
        return indexImgUrl;
    }

    public void setIndexImgUrl(String indexImgUrl) {
        this.indexImgUrl = indexImgUrl;
    }


    public String getListImgUrl() {
        return listImgUrl;
    }

    public void setListImgUrl(String listImgUrl) {
        this.listImgUrl = listImgUrl;
    }


    public List<SupportType> getSupportType() {
        return supportType;
    }

    public void setSupportType(List<SupportType> supportType) {
        this.supportType = supportType;
    }


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public Boolean getIssign() {
        return issign;
    }

    public void setIssign(Boolean issign) {
        this.issign = issign;
    }


    public Boolean getIspush() {
        return ispush;
    }

    public void setIspush(Boolean ispush) {
        this.ispush = ispush;
    }


    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getFirstshowTime() {
        return firstshowTime;
    }

    public void setFirstshowTime(Date firstshowTime) {
        this.firstshowTime = firstshowTime;
    }


    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }


    public Byte getPushType() {
        return pushType;
    }

    public void setPushType(Byte pushType) {
        this.pushType = pushType;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public List<AppointType> getAppointType() {
        return appointType;
    }

    public void setAppointType(List<AppointType> appointType) {
        this.appointType = appointType;
    }


    public List<MerchantHeadimg> getHeadimgs() {
        return headimgs;
    }

    public void setHeadimgs(List<MerchantHeadimg> headimgs) {
        this.headimgs = headimgs;
    }


    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }


    public Long getLandmarkId() {
        return landmarkId;
    }

    public void setLandmarkId(Long landmarkId) {
        this.landmarkId = landmarkId;
    }

    public MerchantVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MerchantVO(Long id) {
        this.id = id;
    }

    public MerchantVO() {
    }
}