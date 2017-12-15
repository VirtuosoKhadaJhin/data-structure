package com.nuanyou.cms.remote.sevenmoor;

import java.util.List;

/**
 * Created by young on 2017/12/15.
 */
public class AddCustomers {

    private String version;

    private List<Customer> customers;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
