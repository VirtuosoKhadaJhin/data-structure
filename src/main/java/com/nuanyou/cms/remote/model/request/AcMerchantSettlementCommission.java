package com.nuanyou.cms.remote.model.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yangkai on 2016/11/3.
 */
public class AcMerchantSettlementCommission {

    private Long id;

    private BigDecimal groupon;

    private Long settlementId;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getGroupon() {
        return groupon;
    }

    public void setGroupon(BigDecimal groupon) {
        this.groupon = groupon;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }


}
