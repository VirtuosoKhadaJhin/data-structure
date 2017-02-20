package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.ItemSupportType;
import com.nuanyou.cms.entity.enums.ItemSupportTypeConverter;
import com.nuanyou.cms.entity.enums.TuanType;
import com.nuanyou.cms.entity.enums.TuanTypeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by Alan.ye on 2016/9/7.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_item")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler", "merchant"})
public class Item {
    private Long id;
    private String name;
    private String kpname;
    private Integer type;
    private Integer itemType;
    private String intro;
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
    private Date createTime;
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

    public Item() {
    }

    public Item(Long id, String name) {
        this.id = id;
        this.name = name;
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


    @Column(name = "name", nullable = false, length = 80)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "kpname", nullable = true, length = 80)
    public String getKpname() {
        return kpname;
    }

    public void setKpname(String kpname) {
        this.kpname = kpname;
    }


    @Column(name = "itemtype", nullable = true)
    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.type = itemType;
        this.itemType = itemType;
    }

    @Column(name = "type", nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "intro", nullable = true, length = 4000)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String Longro) {
        this.intro = Longro;
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
    public BigDecimal getKpPrice() {
        return kpPrice;
    }

    public void setKpPrice(BigDecimal kpPrice) {
        this.kpPrice = kpPrice;
    }


    @Column(name = "notice")
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Column(name = "okpprice", nullable = true, precision = 2)
    public BigDecimal getOkpPrice() {
        return okpPrice;
    }

    public void setOkpPrice(BigDecimal okpPrice) {
        this.okpPrice = okpPrice;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mchid", nullable = true)
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Column(name = "sort", nullable = true)
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "ishot", nullable = true)
    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean hot) {
        isHot = hot;
    }

    @Column(name = "display", nullable = true)
    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    @Column(name = "imgurl", nullable = true, length = 200)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catid", nullable = true)
    public ItemCat getCat() {
        return cat;
    }

    public void setCat(ItemCat cat) {
        this.cat = cat;
    }

    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Column(name = "indeximgurl", nullable = true, length = 200)
    public String getIndexImgUrl() {
        return indexImgUrl;
    }

    public void setIndexImgUrl(String indexImgUrl) {
        this.indexImgUrl = indexImgUrl;
    }


    @Convert(converter = TuanTypeConverter.class)
    @Column(name = "tuantype", nullable = true)
    public List<TuanType> getTuanType() {
        return tuanType;
    }

    public void setTuanType(List<TuanType> tuanType) {
        this.tuanType = tuanType;
    }


    @Column(name = "groupid", nullable = true)
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }


    @Column(name = "templateid", nullable = true)
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }


    @Column(name = "unit", nullable = true, length = 100)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @Column(name = "mchprice", nullable = true, precision = 2)
    public BigDecimal getMchPrice() {
        return mchPrice;
    }

    public void setMchPrice(BigDecimal mchPrice) {
        this.mchPrice = mchPrice;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "starttime", nullable = true)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "endtime", nullable = true)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Convert(converter = ItemSupportTypeConverter.class)
    @Column(name = "supporttype", nullable = true)
    public List<ItemSupportType> getSupportType() {
        return supportType;
    }

    public void setSupportType(List<ItemSupportType> supportType) {
        this.supportType = supportType;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "item", cascade = CascadeType.ALL)
    public ItemDirectmail getDirectmail() {
        return directmail;
    }

    public void setDirectmail(ItemDirectmail directmail) {
        this.directmail = directmail;
    }

    @Column(name = "weight")
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Transient
    public List<ItemDetailimg> getDetailimgs() {
        return detailimgs;
    }

    public void setDetailimgs(List<ItemDetailimg> detailimgs) {
        this.detailimgs = detailimgs;
    }
}
