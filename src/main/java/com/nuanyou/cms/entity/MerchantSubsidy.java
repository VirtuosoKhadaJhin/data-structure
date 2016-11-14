package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
import com.nuanyou.cms.entity.enums.PeriodType;
import com.nuanyou.cms.entity.enums.PeriodTypeConverter;
import com.nuanyou.cms.entity.enums.SubsidyType;
import com.nuanyou.cms.entity.enums.SubsidyTypeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_merchant_subsidy")
public class MerchantSubsidy {

    private Long id;
    private Long mchId;
    private Date startTime;
    private Date endTime;
    private BigDecimal mchSubsidy;
    private BigDecimal mchSubsidyLimit;
    private PeriodType mchSubsidyPeriod;
    private SubsidyType userSubsidyType;
    private Byte userSubsidyDiscount;
    private Integer userSubsidyAmount;
    private Integer userSubsidyLimit;
    private Boolean isUserFirstOrder;
    private String subsidyName;
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


    @Column(name = "mchid", nullable = true)
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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

    @Column(name = "subsidyrate", nullable = true)
    public BigDecimal getMchSubsidy() {
        return mchSubsidy;
    }

    public void setMchSubsidy(BigDecimal mchSubsidy) {
        this.mchSubsidy = mchSubsidy;
    }


    @Column(name = "mchsubsidylimit", nullable = true, precision = 2)
    public BigDecimal getMchSubsidyLimit() {
        return mchSubsidyLimit;
    }

    public void setMchSubsidyLimit(BigDecimal mchSubsidyLimit) {
        this.mchSubsidyLimit = mchSubsidyLimit;
    }


    @Convert(converter = PeriodTypeConverter.class)
    @Column(name = "mchsubsidyperiod", nullable = true)
    public PeriodType getMchSubsidyPeriod() {
        return mchSubsidyPeriod;
    }

    public void setMchSubsidyPeriod(PeriodType mchSubsidyPeriod) {
        this.mchSubsidyPeriod = mchSubsidyPeriod;
    }


    @Convert(converter = SubsidyTypeConverter.class)
    @Column(name = "usersubsidytype", nullable = true)
    public SubsidyType getUserSubsidyType() {
        return userSubsidyType;
    }

    public void setUserSubsidyType(SubsidyType userSubsidyType) {
        this.userSubsidyType = userSubsidyType;
    }


    @Column(name = "usersubsidydiscount", nullable = true)
    public Byte getUserSubsidyDiscount() {
        return userSubsidyDiscount;
    }

    public void setUserSubsidyDiscount(Byte userSubsidyDiscount) {
        this.userSubsidyDiscount = userSubsidyDiscount;
    }


    @Column(name = "usersubsidyamount", nullable = true)
    public Integer getUserSubsidyAmount() {
        return userSubsidyAmount;
    }

    public void setUserSubsidyAmount(Integer userSubsidyAmount) {
        this.userSubsidyAmount = userSubsidyAmount;
    }


    @Column(name = "usersubsidylimit", nullable = true)
    public Integer getUserSubsidyLimit() {
        return userSubsidyLimit;
    }

    public void setUserSubsidyLimit(Integer userSubsidyLimit) {
        this.userSubsidyLimit = userSubsidyLimit;
    }


    @Column(name = "isuserfirstorder", nullable = true)
    public Boolean getIsUserFirstOrder() {
        return isUserFirstOrder;
    }

    public void setIsUserFirstOrder(Boolean isUserFirstOrder) {
        this.isUserFirstOrder = isUserFirstOrder;
    }


    @Column(name = "subsidyname", nullable = true, length = 50)
    public String getSubsidyName() {
        return subsidyName;
    }

    public void setSubsidyName(String subsidyName) {
        this.subsidyName = subsidyName;
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
