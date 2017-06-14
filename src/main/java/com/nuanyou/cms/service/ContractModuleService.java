package com.nuanyou.cms.service;

import com.nuanyou.cms.model.contract.output.Contract;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Felix on 2017/5/31.
 */
public interface ContractModuleService {
    public Page<Contract> getContracts(Long userId, Long merchantId, Long id, String merchantName, String status, String templateid, String type, Boolean businessLicense, Boolean paperContract, String startTime, String endTime, Integer index, Integer limit);

    Contract getContract(Long id);


    Map<String,String> setParamsTitle(Long templateId, Map<String, String> parameters) throws UnsupportedEncodingException;
}
