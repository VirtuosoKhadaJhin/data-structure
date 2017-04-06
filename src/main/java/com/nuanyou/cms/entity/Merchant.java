package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.commons.LastModified;
import com.nuanyou.cms.entity.enums.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_merchant")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Merchant {
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
    private Date updateTime;
    //private BigDecimal ratio;

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

    private BigDecimal postage;

    private BigDecimal mailWeight;

    private String directmailRemind;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 80)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "kpname", nullable = true, length = 80)
    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }

    @Column(name = "intro", nullable = true, length = -1)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String Longro) {
        this.intro = Longro;
    }

    @Column(name = "intro2", nullable = true, length = -1)
    public String getIntro2() {
        return intro2;
    }

    public void setIntro2(String Longro2) {
        this.intro2 = Longro2;
    }

    @Column(name = "outline", nullable = true, length = 200)
    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    @Column(name = "telphone", nullable = true, length = 20)
    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    @DateTimeFormat(pattern = "HH:mm:ss", iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Column(name = "businessstart", nullable = true)
    public Date getBusinessStart() {
        return businessStart;
    }

    public void setBusinessStart(Date businessStart) {
        this.businessStart = businessStart;
    }

    @DateTimeFormat(pattern = "HH:mm:ss", iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Column(name = "businessend", nullable = true)
    public Date getBusinessEnd() {
        return businessEnd;
    }

    public void setBusinessEnd(Date businessEnd) {
        this.businessEnd = businessEnd;
    }

    @Convert(converter = WeekConverter.class)
    @Column(name = "businessday", nullable = true)
    public List<Week> getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(List<Week> businessDay) {
        this.businessDay = businessDay;
    }

    @Convert(converter = VerifyTypeConverter.class)
    @Column(name = "verifytype", nullable = true)
    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }

    @Column(name = "address", nullable = true, length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "kpaddress", nullable = true, length = 200)
    public String getKpaddress() {
        return kpaddress;
    }

    public void setKpaddress(String kpaddress) {
        this.kpaddress = kpaddress;
    }

    @Column(name = "recommend", nullable = true)
    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    @Column(name = "longitude", nullable = true, precision = 0)
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Column(name = "latitude", nullable = true, precision = 0)
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "consume", nullable = true, precision = 2)
    public BigDecimal getConsume() {
        return consume;
    }

    public void setConsume(BigDecimal consume) {
        this.consume = consume;
    }

    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtid")
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topcatid")
    public MerchantCat getMcat() {
        return mcat;
    }

    public void setMcat(MerchantCat mcat) {
        this.mcat = mcat;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcatid")
    public MerchantCat getSubcat() {
        return subcat;
    }

    public void setSubcat(MerchantCat subcat) {
        this.subcat = subcat;
    }


    @Convert(converter = PayTypeConverter.class)
    @Column(name = "paytype", nullable = true)
    public List<PayType> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<PayType> payTypes) {
        this.payTypes = payTypes;
    }

    @Column(name = "indeximgurl", nullable = true, length = 200)
    public String getIndexImgUrl() {
        return indexImgUrl;
    }

    public void setIndexImgUrl(String indexImgUrl) {
        this.indexImgUrl = indexImgUrl;
    }

    @Column(name = "listimgurl", nullable = true, length = 200)
    public String getListImgUrl() {
        return listImgUrl;
    }

    public void setListImgUrl(String listImgUrl) {
        this.listImgUrl = listImgUrl;
    }

    @Convert(converter = SupportTypeConverter.class)
    @Column(name = "supporttype", nullable = true)
    public List<SupportType> getSupportType() {
        return supportType;
    }

    public void setSupportType(List<SupportType> supportType) {
        this.supportType = supportType;
    }

    @Column(name = "tips", nullable = true, length = 255)
    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Column(name = "issign", nullable = true)
    public Boolean getIssign() {
        return issign;
    }

    public void setIssign(Boolean issign) {
        this.issign = issign;
    }

    @Column(name = "ispush", nullable = true)
    public Boolean getIspush() {
        return ispush;
    }

    public void setIspush(Boolean ispush) {
        this.ispush = ispush;
    }

    @Column(name = "channelid", nullable = true)
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @LastModified
    @Column(name = "updatetime")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "firstshowtime", nullable = true)
    public Date getFirstshowTime() {
        return firstshowTime;
    }

    public void setFirstshowTime(Date firstshowTime) {
        this.firstshowTime = firstshowTime;
    }

    @Column(name = "score", nullable = true, precision = 0)
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Column(name = "rank", nullable = false)
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Column(name = "pushtype", nullable = true)
    public Byte getPushType() {
        return pushType;
    }

    public void setPushType(Byte pushType) {
        this.pushType = pushType;
    }

    @Column(name = "code", nullable = true, length = 100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Convert(converter = AppointTypeConverter.class)
    @Column(name = "appointtype", nullable = true)
    public List<AppointType> getAppointType() {
        return appointType;
    }

    public void setAppointType(List<AppointType> appointType) {
        this.appointType = appointType;
    }


//    @OneToMany(fetch = FetchType.LAZY, targetEntity = MerchantHeadimg.class)
//    @JoinColumns(value = {@JoinColumn(name = "mchid", referencedColumnName = "id")})
    @Transient
    public List<MerchantHeadimg> getHeadimgs() {
        return headimgs;
    }

    public void setHeadimgs(List<MerchantHeadimg> headimgs) {
        this.headimgs = headimgs;
    }

    @Column(name = "catid", nullable = true)
    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    @Column(name = "landmarkid", nullable = true)
    public Long getLandmarkId() {
        return landmarkId;
    }

    public void setLandmarkId(Long landmarkId) {
        this.landmarkId = landmarkId;
    }

    public Merchant(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Merchant(Long id) {
        this.id = id;
    }

    public Merchant() {
    }

    @Column(name = "postage")
    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    @Column(name = "mailweight")
    public BigDecimal getMailWeight() {
        return mailWeight;
    }

    public void setMailWeight(BigDecimal mailWeight) {
        this.mailWeight = mailWeight;
    }

    @Column(name = "directmailremind")
    public String getDirectmailRemind() {
        return directmailRemind;
    }

    public void setDirectmailRemind(String directmailRemind) {
        this.directmailRemind = directmailRemind;
    }


//    @Column(name = "ratio")
//    public BigDecimal getRatio() {
//        return ratio;
//    }
//
//    public void setRatio(BigDecimal ratio) {
//        this.ratio = ratio;
//    }
}