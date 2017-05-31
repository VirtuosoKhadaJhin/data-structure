package com.nuanyou.cms.service;

import com.nuanyou.cms.model.contract.output.Contract;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2017/5/31.
 */
public interface ContractModuleService {
    public Page<Contract> getContracts(Long userId, Long merchantId, Long id, String merchantName, String status, String templateid, String type, Boolean businessLicense, Boolean paperContract, String startTime, String endTime, Integer index, Integer limit);

}
