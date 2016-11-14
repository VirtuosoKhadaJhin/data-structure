package com.nuanyou.cms.entity;

import com.nuanyou.cms.commons.DateEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Felix on 2016/9/27.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "ny_rate")
public class Rate {
    private Long id;
    private BigDecimal midrate;
    private BigDecimal yhrate;
    private String code;
    private Integer logid;

    private Country country;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "midrate", nullable = true, precision = 4)
    public BigDecimal getMidrate() {
        return midrate;
    }

    public void setMidrate(BigDecimal midrate) {
        this.midrate = midrate;
    }


    @Column(name = "yhrate", nullable = true, precision = 4)
    public BigDecimal getYhrate() {
        return yhrate;
    }

    public void setYhrate(BigDecimal yhrate) {
        this.yhrate = yhrate;
    }


    @Column(name = "code", nullable = true, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Column(name = "logid", nullable = true)
    public Integer getLogid() {
        return logid;
    }

    public void setLogid(Integer logid) {
        this.logid = logid;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "rate")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    @Transient
    public BigDecimal getNyrate() {
        BigDecimal midrate = this.midrate;
        if (midrate != null && this != null && this.country.getRadio().doubleValue() != 0) {
            BigDecimal nyrate = midrate.divide(this.country.getRadio(), 4, BigDecimal.ROUND_HALF_UP);
            return this.ChangeRate(nyrate);
        }
        return null;
    }

    private BigDecimal ChangeRate(BigDecimal rate) {
        if (rate != null) {
            int size = 4 - String.valueOf(rate.intValue()).length();
            // 如果整数位数大于等于4，就舍弃小数部分
            if (size <= 0) {
                rate = rate.setScale(0, BigDecimal.ROUND_HALF_UP);
            } else {
                rate = rate.setScale(size, BigDecimal.ROUND_HALF_UP);
                DecimalFormat df = new DecimalFormat("#.###");
                rate = new BigDecimal(df.format(rate));
            }
        }
        return rate;
    }


}
