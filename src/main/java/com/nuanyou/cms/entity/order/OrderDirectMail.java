package com.nuanyou.cms.entity.order;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Felix on 2017/2/28.
 */

@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_order_directmail")
public class OrderDirectMail {

    private Long id;
    private Order order;
    private BigDecimal postage;
    private BigDecimal postageRmb;

    private BigDecimal poundage;
    private BigDecimal poundageRmb;
    private Date createTime;
    private BigDecimal weight;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid", nullable = true)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(name = "postage")
    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    @Column(name = "postagermb")
    public BigDecimal getPostageRmb() {
        return postageRmb;
    }

    public void setPostageRmb(BigDecimal postageRmb) {
        this.postageRmb = postageRmb;
    }
    @Column(name = "poundage")
    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }
    @Column(name = "poundagermb")
    public BigDecimal getPoundageRmb() {
        return poundageRmb;
    }

    public void setPoundageRmb(BigDecimal poundageRmb) {
        this.poundageRmb = poundageRmb;
    }
    @Column(name = "createtime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "weight")
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
