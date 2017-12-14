package com.nuanyou.cms.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by young on 2017/11/28.
 */
public class ContractBankAccountModel extends BasicModel {

    @ApiModelProperty("银行编码")
    private String bankCode;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("分行名称")
    private String branchBankName;
    @ApiModelProperty("账号名称")
    private String accountName;
    @ApiModelProperty("账号类型")
    private Integer accountTypeId;
    @ApiModelProperty("账号类型")
    private String accountType;
    @ApiModelProperty("银行账号")
    private String account;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchBankName() {
        return branchBankName;
    }

    public void setBranchBankName(String branchBankName) {
        this.branchBankName = branchBankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }
}
