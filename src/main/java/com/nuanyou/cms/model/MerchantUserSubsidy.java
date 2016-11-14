package com.nuanyou.cms.model;

import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.entity.enums.PayType;
import com.nuanyou.cms.entity.enums.SupportType;
import com.nuanyou.cms.entity.enums.Week;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public class MerchantUserSubsidy {

    private Long id;
    private String name;
    private String kpname;
    private String intro;
    private String intro2;
    private String outline;
    private String telphone;
    private Time businessStart;
    private Time businessEnd;
    private List<Week> businessDay;
    private String address;
    private String kpaddress;
    private Boolean recommend;
    private Double longitude;
    private Double latitude;
    private BigDecimal consume;
    private Integer sort;
    private Boolean display;
    private Long districtId;
    private MerchantCat mcat;
    private List<PayType> payTypes;
    private String indexImgUrl;
    private List<SupportType> supportType;
    private String tips;
    private Boolean issign;
    private Byte ispush;
    private Long channelId;
    private Date createTime;
    private Date firstshowTime;
    private Double score;
    private Byte rank;
    private Byte pushType;
    private String code;
    private Long subcatId;
    private Byte appointType;

    private Byte subsidyUsAge;
    private Byte userSubsidyType;
    private Byte userSubsidyDiscount;
    private BigDecimal userSubsidyAmount;
    private BigDecimal userSubsidyLimit;
    private Byte isuserFirstOrder;
    private Date startTime;
    private Integer num;
    private BigDecimal userSubsidyFloor;


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

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIntro2() {
        return intro2;
    }

    public void setIntro2(String intro2) {
        this.intro2 = intro2;
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

    public Time getBusinessStart() {
        return businessStart;
    }

    public void setBusinessStart(Time businessStart) {
        this.businessStart = businessStart;
    }

    public Time getBusinessEnd() {
        return businessEnd;
    }

    public void setBusinessEnd(Time businessEnd) {
        this.businessEnd = businessEnd;
    }

    public List<Week> getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(List<Week> businessDay) {
        this.businessDay = businessDay;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public MerchantCat getMcat() {
        return mcat;
    }

    public void setMcat(MerchantCat mcat) {
        this.mcat = mcat;
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

    public Byte getIspush() {
        return ispush;
    }

    public void setIspush(Byte ispush) {
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

    public Byte getRank() {
        return rank;
    }

    public void setRank(Byte rank) {
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

    public Long getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(Long subcatId) {
        this.subcatId = subcatId;
    }

    public Byte getAppointType() {
        return appointType;
    }

    public void setAppointType(Byte appointType) {
        this.appointType = appointType;
    }

    public Byte getSubsidyUsAge() {
        return subsidyUsAge;
    }

    public void setSubsidyUsAge(Byte subsidyUsAge) {
        this.subsidyUsAge = subsidyUsAge;
    }

    public Byte getUserSubsidyType() {
        return userSubsidyType;
    }

    public void setUserSubsidyType(Byte userSubsidyType) {
        this.userSubsidyType = userSubsidyType;
    }

    public Byte getUserSubsidyDiscount() {
        return userSubsidyDiscount;
    }

    public void setUserSubsidyDiscount(Byte userSubsidyDiscount) {
        this.userSubsidyDiscount = userSubsidyDiscount;
    }

    public BigDecimal getUserSubsidyAmount() {
        return userSubsidyAmount;
    }

    public void setUserSubsidyAmount(BigDecimal userSubsidyAmount) {
        this.userSubsidyAmount = userSubsidyAmount;
    }

    public BigDecimal getUserSubsidyLimit() {
        return userSubsidyLimit;
    }

    public void setUserSubsidyLimit(BigDecimal userSubsidyLimit) {
        this.userSubsidyLimit = userSubsidyLimit;
    }

    public Byte getIsuserFirstOrder() {
        return isuserFirstOrder;
    }

    public void setIsuserFirstOrder(Byte isuserFirstOrder) {
        this.isuserFirstOrder = isuserFirstOrder;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getUserSubsidyFloor() {
        return userSubsidyFloor;
    }

    public void setUserSubsidyFloor(BigDecimal userSubsidyFloor) {
        this.userSubsidyFloor = userSubsidyFloor;
    }
}
