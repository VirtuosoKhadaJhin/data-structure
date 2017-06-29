package com.nuanyou.cms.entity.mission;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.Merchant;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/6/26.
 */
@Entity
@Table(name = "bd_merchant_track")
@EntityListeners({DateEntityListener.class})
public class BdMerchantTrack {

    public static final Integer ADD = 1;
    public static final Integer EDIT = 2;
    public static final Integer DELETE = 3;

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

    @Column(name = "userid")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "mchid")
    private Merchant merchant;

    @Column(name = "content")
    private String content;

    @Column(name = "operation")
    private Integer operation;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
