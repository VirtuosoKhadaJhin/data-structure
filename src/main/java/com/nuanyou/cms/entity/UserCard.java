package com.nuanyou.cms.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alan.ye on 2016/8/19.
 */
@Entity
@Table(name = "ny_user_card")
@EntityListeners({AuditingEntityListener.class})
public class UserCard {

    private Long id;

    private Byte type;

    private String title;

    private Long mchid;

    private Long userid;

    private String intro;

    private Date createtime;

    private BigDecimal discount;


    private BigDecimal startprice;

    private BigDecimal price;

    private BigDecimal oprice;

    private BigDecimal merchantprice;

    private List<UserCardItem> cardItems;


    private Long merchantCardId;


    private BigDecimal limitPrice;

    private String explains;








    @Column(name = "limitprice")
    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public UserCard() {
    }

    public UserCard(Long id) {
        this.id = id;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "type", nullable = true)
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }


    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Column(name = "mchid", nullable = true)
    public Long getMchid() {
        return mchid;
    }

    public void setMchid(Long mchid) {
        this.mchid = mchid;
    }


    @Column(name = "userid", nullable = true)
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }


    @Column(name = "intro", nullable = true, length = 255)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Column(name = "discount", nullable = true, precision = 2)
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


    @Column(name = "startprice", nullable = true, precision = 2)
    public BigDecimal getStartprice() {
        return startprice;
    }

    public void setStartprice(BigDecimal startprice) {
        this.startprice = startprice;
    }

    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Column(name = "oprice", nullable = true, precision = 2)
    public BigDecimal getOprice() {
        return oprice;
    }

    public void setOprice(BigDecimal oprice) {
        this.oprice = oprice;
    }


    @Column(name = "merchantprice", nullable = true, precision = 2)
    public BigDecimal getMerchantprice() {
        return merchantprice;
    }

    public void setMerchantprice(BigDecimal merchantprice) {
        this.merchantprice = merchantprice;
    }


    @Transient
    public UserCardItem getFristCardItems() {
        if (cardItems != null && !cardItems.isEmpty())
            return cardItems.get(0);
        return null;
    }

    public void addCardItem(UserCardItem item) {
        if (cardItems == null)
            cardItems = new ArrayList<>();
        cardItems.add(item);
    }


    @OneToMany(mappedBy = "userCard", cascade = CascadeType.ALL)
    public List<UserCardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(List<UserCardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @Column(name = "merchantcardid")
    public Long getMerchantCardId() {
        return merchantCardId;
    }

    public void setMerchantCardId(Long merchantCardId) {
        this.merchantCardId = merchantCardId;
    }


    @Column(name="explains")
    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }
}