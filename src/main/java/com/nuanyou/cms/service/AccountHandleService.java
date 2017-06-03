package com.nuanyou.cms.service;

import com.nuanyou.cms.model.contract.output.Contract;

/**
 * Created by Felix on 2017/6/3.
 */
public interface AccountHandleService {
    void addSettlementForAccount(Contract detail);
}
