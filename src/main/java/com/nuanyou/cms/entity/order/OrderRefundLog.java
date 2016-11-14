package com.nuanyou.cms.entity.order;

import com.nuanyou.cms.commons.CreatedAt;
import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Alan.ye on 2016/9/5.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_order_refund_log")
public class OrderRefundLog {

    private Long id;
    private Order order;
    private Integer status;
    private Long cmsuserId;
    private String cmsusername;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderid", nullable = true)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Column(name = "cmsuserid", nullable = true)
    public Long getCmsuserId() {
        return cmsuserId;
    }

    public void setCmsuserId(Long cmsuserId) {
        this.cmsuserId = cmsuserId;
    }

    @Column(name = "cmsusername", nullable = true, length = 50)
    public String getCmsusername() {
        return cmsusername;
    }

    public void setCmsusername(String cmsusername) {
        this.cmsusername = cmsusername;
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
