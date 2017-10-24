package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mylon.sun on 2017/10/23.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_item_tuan_img")
public class ItemTuanImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "itemid")
    private Long itemId;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "imgurl")
    private String imgUrl;

    @Column(name = "createtime")
    private Date createTime;

    @Column(name = "updatetime")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
