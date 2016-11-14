package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.CreatedAt;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@Table(name = "ny_user_recharge_log")
public class UserRechargeLog {

    private Long id;
    private Long userId;
    private BigDecimal amount;
    private Long operatorId;
    private String operatorName;
    private Integer type;
    private Date createTime;

    public UserRechargeLog() {
    }

    public UserRechargeLog(Long userId, BigDecimal amount, Integer type, Long operatorId, String operatorName) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
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


    @Column(name = "userid", nullable = true)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Column(name = "amount", nullable = true, precision = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Column(name = "operatorid", nullable = true)
    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }


    @Column(name = "operatorname", nullable = true, length = 255)
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }


    @Column(name = "type", nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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