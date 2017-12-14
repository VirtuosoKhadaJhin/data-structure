package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by young on 2017/11/28.
 */
public class ContractSettleModel extends BasicModel {

    @ApiModelProperty("费率")
    private String fee;
    @ApiModelProperty("账期")
    private String settlePeriod;
    @ApiModelProperty("账期类型")
    private Integer settleTypeId;
    @ApiModelProperty("账期类型")
    private String settleType;
    @ApiModelProperty("商户英文名")
    private String merchantEnglishName;
    @ApiModelProperty("合同期限")
    private Integer contractPeriodId;
    @ApiModelProperty("合同期限")
    private String contractPeriod;
    @ApiModelProperty("生效日期")
    private Date effectTime;
    @ApiModelProperty("截止日期")
    private Date dueTime;


    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSettlePeriod() {
        return settlePeriod;
    }

    public void setSettlePeriod(String settlePeriod) {
        this.settlePeriod = settlePeriod;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(Date effectTime) {
        this.effectTime = effectTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public String getMerchantEnglishName() {
        return merchantEnglishName;
    }

    public void setMerchantEnglishName(String merchantEnglishName) {
        this.merchantEnglishName = merchantEnglishName;
    }

    public String getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(String contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public Integer getSettleTypeId() {
        return settleTypeId;
    }

    public void setSettleTypeId(Integer settleTypeId) {
        this.settleTypeId = settleTypeId;
    }

    public Integer getContractPeriodId() {
        return contractPeriodId;
    }

    public void setContractPeriodId(Integer contractPeriodId) {
        this.contractPeriodId = contractPeriodId;
    }
}
