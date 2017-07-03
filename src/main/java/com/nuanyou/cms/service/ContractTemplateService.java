package com.nuanyou.cms.service;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequest;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequests;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Felix on 2017/5/31.
 */
public interface ContractTemplateService {
    Page<ContractTemplate> findContractTemplateList(Long countryId, Integer integer, Integer type, Integer index, Integer limit);

    APIResult saveTemplate(String shortCode, List<Long> paramIds, List<TemplateParameterRequest> requests, Integer templateType, String title, Long countryId, Long id);

    List<ContractParameter> getAllParams();

    List<ContractParameter> findAllTemplateParameters(int i, int i1);

    void setLangsMessage(List<ContractParameter> selectedParams);

    ContractParameter getParameterDetail(Long selectedParamId);

    void setLangsMessage(ContractParameter contractParameter);
}
