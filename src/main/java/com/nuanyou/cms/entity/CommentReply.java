package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_comment_reply")
public class CommentReply {

    private Long id;
    private String content;
    private String imgs;
    private Long commentId;
    private boolean deleted;
    private Date createTime;

    public CommentReply() {
    }

    public CommentReply(Long commentId) {
        this.commentId = commentId;
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


    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Column(name = "imgs", nullable = true, length = 255)
    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }


    @Column(name = "commentid", nullable = true)
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Column(name = "isdelete", nullable = true)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}