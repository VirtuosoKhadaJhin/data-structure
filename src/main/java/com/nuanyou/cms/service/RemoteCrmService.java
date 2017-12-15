package com.nuanyou.cms.service;


import com.nuanyou.cms.model.ContactModel;
import com.nuanyou.cms.model.ContractModel;
import com.nuanyou.cms.model.CustomerServiceInfo;

import java.util.List;

/**
 * Created by young on 2017/8/18.
 */
public interface RemoteCrmService {

    CustomerServiceInfo getCustomerServiceInfo(String callNo)  throws Exception;

}
