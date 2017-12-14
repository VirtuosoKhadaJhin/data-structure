package com.nuanyou.cms.model;

import java.util.List;

/**
 * Created by young on 2017/12/14.
 */
public class CustomerServicePage {

    private MerchantVO merchant;

    private List<ContractModel> contracts;

    private List<ContactModel> contacts;

    public MerchantVO getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantVO merchant) {
        this.merchant = merchant;
    }

    public List<ContractModel> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractModel> contracts) {
        this.contracts = contracts;
    }

    public List<ContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactModel> contacts) {
        this.contacts = contacts;
    }
}
