package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_merchant_marker")
public class MerchantMarker {
    private Long id;
    private Integer mchid;
    private Integer userid;
    private String name;
    private String kpname;
    private String Longro;
    private String outline;
    private String telphone;
    private Time businessstart;
    private Time businessend;
    private Integer businessday;
    private String address;
    private String kpaddress;
    private Byte recommend;
    private Double longitude;
    private Double latitude;
    private BigDecimal consume;
    private Integer catid;
    private String indeximgurl;
    private Date createtime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "mchid", nullable = true)
    public Integer getMchid() {
        return mchid;
    }

    public void setMchid(Integer mchid) {
        this.mchid = mchid;
    }


    @Column(name = "userid", nullable = true)
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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


    @Column(name = "Longro", nullable = true, length = -1)
    public String getIntro() {
        return Longro;
    }

    public void setIntro(String Longro) {
        this.Longro = Longro;
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


    @Column(name = "businessstart", nullable = true)
    public Time getBusinessstart() {
        return businessstart;
    }

    public void setBusinessstart(Time businessstart) {
        this.businessstart = businessstart;
    }


    @Column(name = "businessend", nullable = true)
    public Time getBusinessend() {
        return businessend;
    }

    public void setBusinessend(Time businessend) {
        this.businessend = businessend;
    }


    @Column(name = "businessday", nullable = true)
    public Integer getBusinessday() {
        return businessday;
    }

    public void setBusinessday(Integer businessday) {
        this.businessday = businessday;
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
    public Byte getRecommend() {
        return recommend;
    }

    public void setRecommend(Byte recommend) {
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


    @Column(name = "catid", nullable = true)
    public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        this.catid = catid;
    }


    @Column(name = "indeximgurl", nullable = true, length = 200)
    public String getIndeximgurl() {
        return indeximgurl;
    }

    public void setIndeximgurl(String indeximgurl) {
        this.indeximgurl = indeximgurl;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}
