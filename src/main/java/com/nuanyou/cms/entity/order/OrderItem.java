package com.nuanyou.cms.entity.order;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Felix on 2016/9/10.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_order_item")
public class OrderItem {
    private Long id;
    private Order order;
    private Integer itemid;
    private String name;
    private String kpname;
    private String intro;
    private String outline;
    private BigDecimal price;
    private BigDecimal kpprice;
    private Short num;
    private BigDecimal oprice;
    private BigDecimal okpprice;
    private String imgurl;
    private Date createtime;
    private BigDecimal mchprice;
    private BigDecimal mchrmbprice;

    private String specific;


    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    @Column(name = "itemid", nullable = true)
    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }


    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "kpname", nullable = true, length = 255)
    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }


    @Column(name = "intro", nullable = true, length = 4000)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    @Column(name = "outline", nullable = true, length = 200)
    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }


    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Column(name = "kpprice", nullable = true, precision = 2)
    public BigDecimal getKpprice() {
        return kpprice;
    }

    public void setKpprice(BigDecimal kpprice) {
        this.kpprice = kpprice;
    }


    @Column(name = "num", nullable = true)
    public Short getNum() {
        return num;
    }

    public void setNum(Short num) {
        this.num = num;
    }


    @Column(name = "oprice", nullable = true, precision = 2)
    public BigDecimal getOprice() {
        return oprice;
    }

    public void setOprice(BigDecimal oprice) {
        this.oprice = oprice;
    }


    @Column(name = "okpprice", nullable = true, precision = 2)
    public BigDecimal getOkpprice() {
        return okpprice;
    }

    public void setOkpprice(BigDecimal okpprice) {
        this.okpprice = okpprice;
    }


    @Column(name = "imgurl", nullable = true, length = 200)
    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    @Column(name = "mchprice", nullable = true, precision = 2)
    public BigDecimal getMchprice() {
        return mchprice;
    }

    public void setMchprice(BigDecimal mchprice) {
        this.mchprice = mchprice;
    }


    @Column(name = "mchrmbprice", nullable = true, precision = 2)
    public BigDecimal getMchrmbprice() {
        return mchrmbprice;
    }

    public void setMchrmbprice(BigDecimal mchrmbprice) {
        this.mchrmbprice = mchrmbprice;
    }

    @Column(name = "specific")
    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

}