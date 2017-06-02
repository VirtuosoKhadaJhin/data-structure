package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.output.ContractTemplates;
import com.nuanyou.cms.model.contract.request.*;
import com.nuanyou.cms.remote.ContractService;
import com.nuanyou.cms.service.ContractTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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
        APIResult<ContractTemplates> api = contractService.findContractTemplateList(countryId, status, type, index, limit);
        if (api.getCode() != 0) {
            throw new APIException(api.getCode(), api.getMsg());
        }
        ContractTemplates data = api.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<ContractTemplate> list = data.getList();
        setOtherProperties(list);
        return (Page<Contract>) new PageImpl(list, pageable, data.getTotal());
    }

    @Override
    public APIResult saveTemplate1(Long[] selectedParamIds, TemplateParameterRequests templateParameterRequests, Integer templateType, String title, Long countryId, Long id) {
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


    @Override
    public APIResult saveTemplate(List<Long> selectedParamIds, List<Long> paramIds, List<TemplateParameterRequest> requests, Integer templateType, String title, Long countryId, Long id) {

        validateRequest(requests);

        validateBasic(templateType,title,countryId);






        //fetch param ids
        BatchTemplateParameterRequest batch = new BatchTemplateParameterRequest();
        batch.setParameterRequests(requests);
        APIResult<List<Long>> idList = this.contractService.saveTemplateParamters(batch);
        if (idList.getCode() != 0) {
            throw new APIException(idList.getCode(), idList.getMsg());
        }
        List<Long> idsSum = idList.getData();//ids from added param ids
        if (selectedParamIds != null && selectedParamIds.size() > 0) {
            idsSum.addAll(selectedParamIds);//ids from selectedParam ids
        }

        List<Long> originalParamIds = paramIds;
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

    private void validateBasic(Integer templateType, String title, Long countryId) {

        if (templateType == null) {
            throw new APIException(ResultCodes.Fail, "类型不能为空");
        } else if (StringUtils.isBlank(title)) {
            throw new APIException(ResultCodes.Fail, "title不能为空");
        } else if (countryId == null) {
            throw new APIException(ResultCodes.Fail, "国家不能为空");
        }
    }

    private void validateRequest(List<TemplateParameterRequest> requests) {
        Set<String> set=new HashSet<>();
        for (TemplateParameterRequest request : requests) {
            if (StringUtils.isEmpty(request.getName())) {
                throw new APIException(ResultCodes.Fail, "参数名不能为空");
            }
            if (StringUtils.isBlank(request.getKey())) {
                throw new APIException(ResultCodes.Fail, "Key不能为空");
            }

            if (!request.isEditable()&& StringUtils.isBlank(request.getDefaultValue())) {
                throw new APIException(ResultCodes.Fail, "不可编辑的列必须要有默认值");
            }
            set.add(request.getKey());

        }
        if(set.size()!=requests.size()){
            throw new APIException(ResultCodes.Fail, "key不可重复");
        }
    }


    private List<TemplateParameterRequest> toListTemplateParameter(TemplateParameterRequests templateParameterRequests) {

        if (templateParameterRequests.getKey() == null) {
            return new ArrayList<>();

        }
        Integer size = templateParameterRequests.getKey().size();
        List<TemplateParameterRequest> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TemplateParameterRequest request = new TemplateParameterRequest();
            request.setName(templateParameterRequests.getName().get(i));
            request.setType(templateParameterRequests.getDataType().get(i));
            //request.setMultiValuable(templateParameterRequests.getMultiValuable().get(i));
            request.setEditable(templateParameterRequests.getEditable().get(i));
            request.setDefaultValue(templateParameterRequests.getDefaultValue().get(i));
            request.setKey(templateParameterRequests.getKey().get(i));
            request.setNullable(templateParameterRequests.getNullable().get(i));
            //request.setRegex(templateParameterRequests.getRegex().get(i));
            request.setHint(templateParameterRequests.getHint().get(i));
            request.setRemark(templateParameterRequests.getRemark().get(i));
            list.add(request);
        }
        return list;

    }

    private List<Long> removeSame(List<Long> idsSum) {
        HashSet<Long> longs = new HashSet<>(idsSum);
        longs.remove(null);
        return new ArrayList<>(longs);

    }

    private void setOtherProperties(List<ContractTemplate> list) {
        for (int i = 0; i < list.size(); i++) {
            ContractTemplate contractTemplate = list.get(i);
            if (contractTemplate != null) {
                if (contractTemplate.getCountryId() != null) {
                    Country one = countryDao.findOne(list.get(i).getCountryId());
                    contractTemplate.setCountryName(one.getName());
                    if (contractTemplate.getStatus() != null) {
                        contractTemplate.setTemplateStatus(TemplateStatus.toEnum(contractTemplate.getStatus()));
                    }

                }
            }
        }
    }

}
