package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.output.ContractTemplates;
import com.nuanyou.cms.model.contract.request.*;
import com.nuanyou.cms.remote.ContractService;
import com.nuanyou.cms.service.ContractTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Felix on 2017/5/31.
 */
@Service
public class ContractTemplateServiceImpl implements ContractTemplateService {
    @Autowired
    private ContractService contractService;
    @Autowired
    private CountryDao countryDao;
    @Override
    public Page<Contract> findContractTemplateList(Long countryId, Integer status, Integer type, Integer index, Integer limit) {
        APIResult<ContractTemplates> api = contractService.findContractTemplateList(countryId,status, type, index, limit);
        if (api.getCode() != 0) {
            throw new APIException(api.getCode(), api.getMsg());
        }
        ContractTemplates data = api.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<ContractTemplate> list = data.getList();
        setCountryName(list);
        return (Page<Contract>) new PageImpl(list, pageable, data.getTotal());
    }

    @Override
    public APIResult saveTemplate(Long[] selectedParamIds, TemplateParameterRequests templateParameterRequests, Integer templateType, String title, Long countryId, Long id) {
        //fetch param ids
        List<TemplateParameterRequest> list = toListTemplateParameter(templateParameterRequests);
        BatchTemplateParameterRequest batch = new BatchTemplateParameterRequest();
        batch.setParameterRequests(list);
        APIResult<List<Long>> idList = this.contractService.saveTemplateParamters(batch);
        if (idList.getCode() != 0) {
            throw new APIException(idList.getCode(), idList.getMsg());
        }
        List<Long> idsSum = idList.getData();//ids from added param ids
        if (selectedParamIds != null && selectedParamIds.length > 0) {
            idsSum.addAll(Arrays.asList(selectedParamIds));//ids from selectedParam ids
        }

        List<Long> originalParamIds = templateParameterRequests.getParamId();
        if (originalParamIds != null && !originalParamIds.isEmpty()) {
            idsSum.addAll(originalParamIds);//ids from original param ids
        }
        idsSum = removeSame(idsSum);


        Long newVersionId = null;
        if (id == null) {
            //save template
            TemplateRequest templateRequest = new TemplateRequest();
            templateRequest.setParamterids(idsSum);
            templateRequest.setType(templateType);
            templateRequest.setCountryId(countryId);
            templateRequest.setTitle(title);
            APIResult<ContractTemplate> res = this.contractService.saveTemplate(templateRequest);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            newVersionId = res.getData().getId();
        } else {
            //update template
            TemplateUpdateRequest request = new TemplateUpdateRequest();
            request.setTitle(title);
            request.setParamterids(idsSum);
            APIResult<ContractTemplate> res = this.contractService.updateTemplate(id, request);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            newVersionId = res.getData().getId();
        }
        return new APIResult(newVersionId);
    }



    private List<TemplateParameterRequest> toListTemplateParameter(TemplateParameterRequests templateParameterRequests) {
        if (templateParameterRequests.getKey() == null) {
            return new ArrayList<>();

        }
        Integer size = templateParameterRequests.getKey().size();
        List<TemplateParameterRequest> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TemplateParameterRequest request = new TemplateParameterRequest();
            if (templateParameterRequests.getParamId().get(i) != null) {
                continue;
            }
            request.setName(templateParameterRequests.getName().get(i));
            request.setType(templateParameterRequests.getDataType().get(i));
            request.setMultiValuable(templateParameterRequests.getMultiValuable().get(i));
            request.setEditable(templateParameterRequests.getEditable().get(i));
            request.setDefaultValue(templateParameterRequests.getHint().get(i));
            request.setKey(templateParameterRequests.getKey().get(i));
            request.setNullable(templateParameterRequests.getNullable().get(i));
            request.setRegex(templateParameterRequests.getRegex().get(i));
            request.setHint(templateParameterRequests.getHint().get(i));
            list.add(request);
        }
        return list;

    }
    private List<Long> removeSame(List<Long> idsSum) {
        HashSet<Long> longs = new HashSet<>(idsSum);
        longs.remove(null);
        return new ArrayList<>(longs);

    }
    private void setCountryName(List<ContractTemplate> list) {
        for (int i = 0; i < list.size(); i++) {
            ContractTemplate contractTemplate = list.get(i);
            if (contractTemplate != null) {
                if (contractTemplate.getCountryId() != null) {
                    Country one = countryDao.findOne(list.get(i).getCountryId());
                    contractTemplate.setCountryName(one.getName());
                    if(contractTemplate.getStatus()!=null){
                        contractTemplate.setTemplateStatus(TemplateStatus.toEnum(contractTemplate.getStatus()));
                    }

                }
            }
        }
    }

}
