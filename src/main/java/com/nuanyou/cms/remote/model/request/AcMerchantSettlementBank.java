package com.nuanyou.cms.remote.model.request;

/**
 * Created by Felix on 2017/6/26.
 */
public class AcMerchantSettlementBank {
    private Long settlementId;
    private String bankCode;
    private String branch;
    private String account;
    private String name;

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getBranch() {
        return branch;
    }
}
