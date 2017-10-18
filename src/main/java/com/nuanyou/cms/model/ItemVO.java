package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.entity.ItemCat;
import com.nuanyou.cms.entity.ItemDetailimg;
import com.nuanyou.cms.entity.ItemDirectmail;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by Alan.ye on 2016/9/7.
 */
public class ItemVO {
    private Long id;
    private String name;
    private String kpname;
    private Integer type;
    private Integer itemType;
    private String intro;
    private String localIntro;
    private String outline;
    private BigDecimal price;
    private BigDecimal kpPrice;
    private BigDecimal okpPrice;
    private Merchant merchant;
    private Integer sort;
    private Boolean isHot;
    private Boolean display;
    private String imgUrl;
    private ItemCat cat;
    private String indexImgUrl;
    private List<TuanType> tuanType;
    private Long groupId;
    private Long templateId;
    private String unit;
    private BigDecimal mchPrice;
    private Date startTime;
    private Date endTime;
    private String notice;

    private List<ItemSupportType> supportType;

    private ItemDirectmail directmail;

    private BigDecimal weight;

    private List<ItemDetailimg> detailimgs;

    private List<String> spec;

    public ItemVO() {
    }

    public ItemVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }


    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.type = itemType;
        this.itemType = itemType;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String Longro) {
        this.intro = Longro;
    }

    public String getLocalIntro() {
        return localIntro;
    }

    public void setLocalIntro(String localIntro) {
        this.localIntro = localIntro;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public BigDecimal getKpPrice() {
        return kpPrice;
    }

    public void setKpPrice(BigDecimal kpPrice) {
        this.kpPrice = kpPrice;
    }


    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


    public BigDecimal getOkpPrice() {
        return okpPrice;
    }

    public void setOkpPrice(BigDecimal okpPrice) {
        this.okpPrice = okpPrice;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean hot) {
        isHot = hot;
    }


    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ItemCat getCat() {
        return cat;
    }

    public void setCat(ItemCat cat) {
        this.cat = cat;
    }


    public String getIndexImgUrl() {
        return indexImgUrl;
    }

    public void setIndexImgUrl(String indexImgUrl) {
        this.indexImgUrl = indexImgUrl;
    }


    public List<TuanType> getTuanType() {
        return tuanType;
    }

    public void setTuanType(List<TuanType> tuanType) {
        this.tuanType = tuanType;
    }


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }


    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public BigDecimal getMchPrice() {
        return mchPrice;
    }

    public void setMchPrice(BigDecimal mchPrice) {
        this.mchPrice = mchPrice;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<ItemSupportType> getSupportType() {
        return supportType;
    }

    public void setSupportType(List<ItemSupportType> supportType) {
        this.supportType = supportType;
    }

    public ItemDirectmail getDirectmail() {
        return directmail;
    }

    public void setDirectmail(ItemDirectmail directmail) {
        this.directmail = directmail;
    }


    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public List<ItemDetailimg> getDetailimgs() {
        return detailimgs;
    }

    public void setDetailimgs(List<ItemDetailimg> detailimgs) {
        this.detailimgs = detailimgs;
    }

    public List<String> getSpec() {
        return spec;
    }

    public void setSpec(List<String> spec) {
        this.spec = spec;
    }

}