package com.nuanyou.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;
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
@Table(name = "ny_user_subsidy")
public class UserSubsidy {

    private Long id;
    /**
     * 商户ID
     */
    private Long mchId;
    /**
     * 用户补贴类型，1：优付用户补贴，2：商户用户补贴
     */
    private Integer subsidyUsAge;
    /**
     * 用户补贴方式: 0无优惠，1折扣，2固定金额
     */
    private SubsidyType userSubsidyType;
    /**
     * 用户补贴方式为折扣时的折扣，即原价的N%
     */
    private BigDecimal userSubsidyDiscount;
    /**
     * 用户补贴方式为固定金额时的补贴金额
     */
    private BigDecimal userSubsidyAmount;
    /**
     * 用户补贴上限
     */
    private BigDecimal userSubsidyLimit;
    /**
     * 用户补贴下限
     */
    private BigDecimal userSubsidyFloor;
    /**
     * 用户补贴是否仅首单，0：不是，1：是
     */
    private Boolean isUserFirstOrder;
    /**
     * 用户补贴开始时间
     */
    private Date startTime;
    /**
     * 用户补贴结束时间
     */
    private Date endTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 优付次数
     */
    private Integer num;


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


    @Column(name = "subsidyusage", nullable = true)
    public Integer getSubsidyUsAge() {
        return subsidyUsAge;
    }

    public void setSubsidyUsAge(Integer subsidyUsAge) {
        this.subsidyUsAge = subsidyUsAge;
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
    public BigDecimal getUserSubsidyDiscount() {
        return userSubsidyDiscount;
    }

    public void setUserSubsidyDiscount(BigDecimal userSubsidyDiscount) {
        this.userSubsidyDiscount = userSubsidyDiscount;
    }


    @Column(name = "usersubsidyamount", nullable = true, precision = 2)
    public BigDecimal getUserSubsidyAmount() {
        return userSubsidyAmount;
    }

    public void setUserSubsidyAmount(BigDecimal userSubsidyAmount) {
        this.userSubsidyAmount = userSubsidyAmount;
    }


    @Column(name = "usersubsidylimit", nullable = true, precision = 2)
    public BigDecimal getUserSubsidyLimit() {
        return userSubsidyLimit;
    }

    public void setUserSubsidyLimit(BigDecimal userSubsidyLimit) {
        this.userSubsidyLimit = userSubsidyLimit;
    }


    @Column(name = "isuserfirstorder", nullable = true)
    public Boolean getIsUserFirstOrder() {
        return isUserFirstOrder;
    }

    public void setIsUserFirstOrder(Boolean isUserFirstOrder) {
        this.isUserFirstOrder = isUserFirstOrder;
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


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Column(name = "num", nullable = true)
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    @Column(name = "usersubsidyfloor", nullable = true, precision = 2)
    public BigDecimal getUserSubsidyFloor() {
        return userSubsidyFloor;
    }

    public void setUserSubsidyFloor(BigDecimal userSubsidyFloor) {
        this.userSubsidyFloor = userSubsidyFloor;
    }

}
