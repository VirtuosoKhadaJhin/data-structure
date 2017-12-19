package com.nuanyou.cms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by young on 2017/9/27.
 */
public class ContractModel extends BasicModel {
    @ApiModelProperty("合同标识")
    private Long id;
    @ApiModelProperty("服务类型")
    private String serviceTypeName;
    @ApiModelProperty("服务类型编码")
    private String serviceTypeCode;
    @ApiModelProperty("商户名称")
    private String mchName;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("状态编码")
    private String statusCode;
    @ApiModelProperty("商户ID")
    private Long mchId;
    @ApiModelProperty("合同内容")
    private String content;
    @ApiModelProperty("创建人")
    private String userName;
    @ApiModelProperty("合同编号")
    private String code;
    @ApiModelProperty("信息是否完整")
    private Boolean isComplete;
    @ApiModelProperty("是否提审")
    private Boolean isSubmit;
    @ApiModelProperty("创建日期")
    private Date createDate;

    private ContractSettleModel contractSettle = new ContractSettleModel();

    private ContractBankAccountModel contractBankAccount = new ContractBankAccountModel();



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ContractSettleModel getContractSettle() {
        return contractSettle;
    }

    public void setContractSettle(ContractSettleModel contractSettle) {
        this.contractSettle = contractSettle;
    }

    public ContractBankAccountModel getContractBankAccount() {
        return contractBankAccount;
    }

    public void setContractBankAccount(ContractBankAccountModel contractBankAccount) {
        this.contractBankAccount = contractBankAccount;
    }
}
