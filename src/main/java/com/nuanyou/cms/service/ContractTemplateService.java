package com.nuanyou.cms.service;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequests;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2017/5/31.
 */
public interface ContractTemplateService {
    Page<Contract> findContractTemplateList(Long countryId, Integer integer, Integer type, Integer index, Integer limit);

    APIResult saveTemplate(Long[] selectedParamIds, TemplateParameterRequests templateParameterRequests, Integer templateType, String title, Long countryId, Long id);
}
