package com.nuanyou.cms.service;


import com.nuanyou.cms.model.CustomerServiceInfo;

/**
 * Created by young on 2017/8/18.
 */
public interface RemoteCrmService {

    CustomerServiceInfo getCustomerServiceInfo(String callNo)  throws Exception;

}
