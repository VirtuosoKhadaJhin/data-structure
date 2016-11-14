package com.nuanyou.cms.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Felix on 2016/9/8.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Table(name = "ny_order_subsidy")
public class OrderSubsidy {
    private Long id;
    private Order order;
    private Integer mchid;
    private Integer userid;
    private BigDecimal youfusubsidyprice;
    private BigDecimal youfusubsidykpprice;
    private BigDecimal mchsubsidyprice;
    private BigDecimal mchsubsidykpprice;
    private Date createtime;
    private Byte status;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    @Column(name = "mchid", nullable = true)
    public Integer getMchid() {
        return mchid;
    }

    public void setMchid(Integer mchid) {
        this.mchid = mchid;
    }


    @Column(name = "userid", nullable = true)
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }


    @Column(name = "youfusubsidyprice", nullable = true, precision = 2)
    public BigDecimal getYoufusubsidyprice() {
        return youfusubsidyprice;
    }

    public void setYoufusubsidyprice(BigDecimal youfusubsidyprice) {
        this.youfusubsidyprice = youfusubsidyprice;
    }


    @Column(name = "youfusubsidykpprice", nullable = true, precision = 5)
    public BigDecimal getYoufusubsidykpprice() {
        return youfusubsidykpprice;
    }

    public void setYoufusubsidykpprice(BigDecimal youfusubsidykpprice) {
        this.youfusubsidykpprice = youfusubsidykpprice;
    }


    @Column(name = "mchsubsidyprice", nullable = true, precision = 2)
    public BigDecimal getMchsubsidyprice() {
        return mchsubsidyprice;
    }

    public void setMchsubsidyprice(BigDecimal mchsubsidyprice) {
        this.mchsubsidyprice = mchsubsidyprice;
    }


    @Column(name = "mchsubsidykpprice", nullable = true, precision = 5)
    public BigDecimal getMchsubsidykpprice() {
        return mchsubsidykpprice;
    }

    public void setMchsubsidykpprice(BigDecimal mchsubsidykpprice) {
        this.mchsubsidykpprice = mchsubsidykpprice;
    }


    @CreatedAt
    @Column(name = "createtime", nullable = true)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }


}
