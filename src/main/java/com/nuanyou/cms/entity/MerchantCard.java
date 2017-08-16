package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.CardType;
import com.nuanyou.cms.entity.enums.CardTypeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_merchant_card")
public class MerchantCard {
    private Long id;
    private CardType type;
    private Boolean display;
    private String title;
    private Merchant merchant;
    private String intro;
    private String explains;
    private String code;
    private BigDecimal discount;
    private BigDecimal startPrice;
    private BigDecimal price;
    private BigDecimal oPrice;
    private BigDecimal merchantPrice;
    private BigDecimal limitPrice;
    private Integer num;
    private Date validTime;
    private Date createTime;
    private boolean deleted = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Convert(converter = CardTypeConverter.class)
    @Column(name = "type", nullable = true)
    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }


    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mchid")
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "intro", nullable = true, length = 255)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String Longro) {
        this.intro = Longro;
    }

    @Column(name = "explains", nullable = true, length = 2000)
    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "validtime", nullable = true)
    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Column(name = "code", nullable = true, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Column(name = "discount", nullable = true, precision = 2)
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


    @Column(name = "startprice", nullable = true, precision = 2)
    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }


    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Column(name = "oprice", nullable = true, precision = 2)
    public BigDecimal getoPrice() {
        return oPrice;
    }

    public void setoPrice(BigDecimal oPrice) {
        this.oPrice = oPrice;
    }


    @Column(name = "merchantprice", nullable = true, precision = 2)
    public BigDecimal getMerchantPrice() {
        return merchantPrice;
    }

    public void setMerchantPrice(BigDecimal merchantPrice) {
        this.merchantPrice = merchantPrice;
    }


    @Column(name = "limitprice", nullable = true, precision = 2)
    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    @Column(name = "num", nullable = true)
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    @Column(name = "deleted", nullable = true)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
