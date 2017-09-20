package com.nuanyou.cms.entity;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/9/1.
 */
@Entity
@Table(name = "bd_merchant_track")
@EntityListeners({AuditingEntityListener.class})
public class MerchantVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "sign_img_urls")
    private String signImgUrls;
    @Column(name = "note")
    private String note;
    @Column(name = "updatetime")
    private Date updateTime;
    @Column(name = "createtime")
    private Date createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private BdUser user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mchid")
    private Merchant merchant;
    @Column(name = "content")
    private String content;
    @Column(name = "operation")
    private Integer operation;
    @ManyToOne
    @JoinColumn(name="type")
    private VisitType type;
    @Transient
    private Integer visitCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignImgUrls() {
        return signImgUrls;
    }

    public void setSignImgUrls(String signImgUrls) {
        this.signImgUrls = signImgUrls;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BdUser getUser() {
        return user;
    }

    public void setUser(BdUser user) {
        this.user = user;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public List<String> getImgUrls(){
        if(StringUtils.isEmpty(signImgUrls)){
            return Lists.newArrayList();
        }
        return Lists.newArrayList(signImgUrls.split(","));
    }

    public VisitType getType() {
        return type;
    }

    public void setType(VisitType type) {
        this.type = type;
    }


    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }
}
