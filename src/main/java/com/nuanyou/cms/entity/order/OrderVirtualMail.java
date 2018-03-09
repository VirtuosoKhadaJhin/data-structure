package com.nuanyou.cms.entity.order;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;

/**
 * Created by mylon.sun on 2018/1/10.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_order_virtual_mail")
public class OrderVirtualMail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orderid")
    private Long orderId;

    @Column(name = "email")
    private String email;

    @Column(name = "tripdate")
    private String tripDate;

    @Column(name = "codeimgurl")
    private String codeImgUrl;

    @Column(name = "ispush")
    private Boolean isPush;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getCodeImgUrl() {
        return codeImgUrl;
    }

    public void setCodeImgUrl(String codeImgUrl) {
        this.codeImgUrl = codeImgUrl;
    }

    public Boolean getPush() {
        return isPush;
    }

    public void setPush(Boolean push) {
        isPush = push;
    }
}
