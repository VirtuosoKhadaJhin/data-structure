package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.ChannelType;
import com.nuanyou.cms.entity.enums.ChannelTypeConverter;
import com.nuanyou.cms.entity.enums.CodeType;
import com.nuanyou.cms.entity.enums.CodeTypeConverter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_channel")
public class Channel {
    private Long id;
    private String title;
    private ChannelType channelType;
    private String ticket;
    private String url;
    private CodeType qrCodeType;
    private Date expireTime;
    private String sceneId;
    private String groupId;
    private String keyword;
    private Date createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Convert(converter = ChannelTypeConverter.class)
    @Column(name = "channeltype", nullable = false)
    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }


    @Column(name = "ticket", nullable = true, length = 500)
    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }


    @Column(name = "url", nullable = true, length = 500)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Convert(converter = CodeTypeConverter.class)
    @Column(name = "qrcodetype", nullable = true)
    public CodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(CodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }


    @Column(name = "expiretime", nullable = true)
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }


    @Column(name = "sceneid", nullable = true, length = 50)
    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }


    @Column(name = "groupid", nullable = true, length = 50)
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    @Column(name = "keyword", nullable = true, length = 100)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
