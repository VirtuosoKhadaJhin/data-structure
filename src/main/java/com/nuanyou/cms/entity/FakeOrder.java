package com.nuanyou.cms.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Felix on 2016/10/27.
 */
@Entity
@Table(name = "ny_fake_order")
public class FakeOrder {
    private Long id;
    private Merchant merchant ;
    private Integer productid=0;
    private FakeUser fakeUser;
    private String comment;
    private Byte score;
    private Byte anonymous;
    private Date createtime;
    private Date paytime;
    private String imgs;
    private Date commenttime;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid", nullable = true)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "productid")
    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    public FakeUser getFakeUser() {
        return fakeUser;
    }

    public void setFakeUser(FakeUser fakeUser) {
        this.fakeUser = fakeUser;
    }



    @Basic
    @Column(name = "comment", nullable = true, length = 400)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "score", nullable = true)
    public Byte getScore() {
        return score;
    }

    public void setScore(Byte score) {
        this.score = score;
    }

    @Basic
    @Column(name = "anonymous", nullable = true)
    public Byte getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Byte anonymous) {
        this.anonymous = anonymous;
    }

    @Basic
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "paytime", nullable = true)
    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    @Basic
    @Column(name = "imgs", nullable = true, length = 500)
    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    @Basic
    @Column(name = "commenttime", nullable = true)
    public Date getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(Timestamp commenttime) {
        this.commenttime = commenttime;
    }


}
