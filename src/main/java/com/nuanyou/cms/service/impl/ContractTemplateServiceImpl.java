package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.ParamsDataMappingDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.user.ParamsDataMapping;
import com.nuanyou.cms.model.LangsDictionaryVo;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractParameters;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.output.ContractTemplates;
import com.nuanyou.cms.model.contract.request.*;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.ContractTemplateService;
import com.nuanyou.cms.service.LangsDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Felix on 2017/5/31.
 */
@Service
public class ContractTemplateServiceImpl implements ContractTemplateService {
    @Autowired
    private RemoteContractService contractService;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private ParamsDataMappingDao dataMappingDao;
    @Autowired
    private LangsDictionaryService dictionaryService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public Page<ContractTemplate> findContractTemplateList(Long countryId, Integer status, Integer type, Integer index, Integer limit) {
        APIResult<ContractTemplates> api = contractService.findContractTemplateList(countryId, status, type, index, limit);
        if (api.getCode() != 0) {
            throw new APIException(api.getCode(), api.getMsg());
        }
        ContractTemplates data = api.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<ContractTemplate> list = data.getList();
        setOtherProperties(list);
        return new PageImpl<ContractTemplate>(list, pageable, data.getTotal());
    }


    @Override
    public ContractParameter getParameterDetail(Long selectedParamId) {
        APIResult<ContractParameter> res = this.contractService.saveTemplateParamter(selectedParamId);
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        ContractParameter data = res.getData();
        setLangsMessage(data);
        return data;
    }




    @Override
    public APIResult saveTemplate(String shortCode, List<Long> paramIds, List<TemplateParameterRequest> requests, Integer templateType, String title, Long countryId, Long id) {
        // 1 验证表单信息
        validateRequest(paramIds,requests);
        //2 验证模板基本信息
        validateBasic(shortCode,templateType, title, countryId);
        //3 保存参数且返回增加的param ids
        BatchTemplateParameterRequest batch = new BatchTemplateParameterRequest();
        batch.setParameterRequests(requests);
        APIResult<List<Long>> idList = this.contractService.saveTemplateParamters(batch);
        if (idList.getCode() != 0) {
            throw new APIException(idList.getCode(), idList.getMsg());
        }
        List<Long> idsSum = idList.getData();//ids from added param ids
        List<Long> originalParamIds = paramIds;
        if (originalParamIds != null && !originalParamIds.isEmpty()) {
            idsSum.addAll(originalParamIds);//ids from original param ids
        }
        idsSum = removeSame(idsSum);
        Long newVersionId = null;
        if (id == null) {
            //4 save template
            TemplateRequest templateRequest = new TemplateRequest();
            templateRequest.setParamterids(idsSum);
            templateRequest.setType(templateType);
            templateRequest.setCountryId(countryId);
            templateRequest.setShortCode(shortCode);
            templateRequest.setTitle(title);
            APIResult<ContractTemplate> res = this.contractService.saveTemplate(templateRequest);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            newVersionId = res.getData().getId();
        } else {
            //5 update template
            TemplateUpdateRequest request = new TemplateUpdateRequest();
            request.setTitle(title);
            request.setParamterids(idsSum);
            request.setShortCode(shortCode);
            APIResult<ContractTemplate> res = this.contractService.updateTemplate(id, request);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            newVersionId = res.getData().getId();
        }
        return new APIResult(newVersionId);
    }

    @Override
    public List<ContractParameter> getAllParams() {
        APIResult<ContractParameters> allTemplateParameters = this.contractService.findAllTemplateParameters(1, 100000);
        if (allTemplateParameters.getCode() != 0) {
            throw new APIException(allTemplateParameters.getCode(), allTemplateParameters.getMsg());
        }
        ContractParameters data = allTemplateParameters.getData();
        List<ContractParameter> params = data.getList();
        return params;
    }

    @Override
    public List<ContractParameter> findAllTemplateParameters(int index, int size) {
        APIResult<ContractParameters> allTemplateParameters = this.contractService.findAllTemplateParameters(index, size);
        if (allTemplateParameters.getCode() != 0) {
            throw new APIException(allTemplateParameters.getCode(), allTemplateParameters.getMsg());
        }
        ContractParameters data = allTemplateParameters.getData();
        List<ContractParameter> list = data.getList();
        //setLangsMessage(list);
        return list;
    }

    private void validateBasic(String shortCode, Integer templateType, String title, Long countryId) {

        if (templateType == null) {
            throw new APIException(ResultCodes.Fail, "类型不能为空");
        } else if (StringUtils.isBlank(title)) {
            throw new APIException(ResultCodes.Fail, "title不能为空");
        } else if (countryId == null) {
            throw new APIException(ResultCodes.Fail, "国家不能为空");
        }else if (countryId == null) {
            throw new APIException(ResultCodes.Fail, "国家不能为空");
        }else if (StringUtils.isBlank(shortCode)) {
            throw new APIException(ResultCodes.Fail, "简称不能为空");
        }
    }

    private void validateRequest(List<Long> paramIds, List<TemplateParameterRequest> requests) {
        Set<Long> paramIdsSet=new HashSet<>(paramIds);
        Set<String> keySet = new HashSet<>();
        for (TemplateParameterRequest request : requests) {
            if (StringUtils.isEmpty(request.getName())) {
                throw new APIException(ResultCodes.Fail, "参数名无效或者不存在");
            }
            if (StringUtils.isBlank(request.getKey())) {
                throw new APIException(ResultCodes.Fail, "Key不能为空");
            }
            if (!request.isEditable() && StringUtils.isBlank(request.getDefaultValue())) {
                throw new APIException(ResultCodes.Fail, "不可编辑的列必须要有默认值");
            }
            if (!request.isEditable() && StringUtils.isBlank(request.getDefaultValue())) {
                throw new APIException(ResultCodes.Fail, "不可编辑的列必须要有默认值");
            }
            boolean keyCodeValidate = dictionaryService.verifykeyCode(new LangsDictionaryVo(request.getName()));
            if (keyCodeValidate) {
                throw new APIException(ResultCodes.Fail, "参数名:"+request.getName()+"多语言的Key不正确");
            }
            boolean remarkValidate = dictionaryService.verifykeyCode(new LangsDictionaryVo(request.getRemark()));
            if (remarkValidate) {
                throw new APIException(ResultCodes.Fail, "栏位校验:"+request.getRemark()+"多语言的Key不正确");
            }
            if(request.getReferenceId()!=null){
                if(!paramIdsSet.contains(request.getReferenceId())){
                    throw new APIException(ResultCodes.Fail, "引用参数的ID不存在"+request.getReferenceId());
                }
            }

            Long dataMappingId = request.getDateTypeMappingId();
            if (dataMappingId != null) {
                ParamsDataMapping paramsDataMapping = this.dataMappingDao.findOne(dataMappingId);
                request.setRegex(paramsDataMapping.getRegex());
                request.setType(paramsDataMapping.getDataType());
            }
            keySet.add(request.getKey());
        }
        if (keySet.size() != requests.size()) {
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


    @Override
    public void setLangsMessage(List<ContractParameter> langsMessage) {
        for (ContractParameter contractParameter : langsMessage) {
            String name = dictionaryService.findLocalMessageByKeyCode(contractParameter.getName().getKey(), request.getLocale());
            String remark = dictionaryService.findLocalMessageByKeyCode(contractParameter.getRemark().getKey(), request.getLocale());
            contractParameter.getName().setContent(name == null ? contractParameter.getName().getKey() : name);
            contractParameter.getRemark().setContent(remark == null ? contractParameter.getRemark().getKey() : remark);
        }
    }


    @Override
    public void setLangsMessage(ContractParameter contractParameter) {
        String name = dictionaryService.findLocalMessageByKeyCode(contractParameter.getName().getKey(), request.getLocale());
        String remark = dictionaryService.findLocalMessageByKeyCode(contractParameter.getRemark().getKey(), request.getLocale());
        String hint = dictionaryService.findLocalMessageByKeyCode(contractParameter.getHint().getKey(), request.getLocale());
        contractParameter.getName().setContent(name == null ? contractParameter.getName().getKey() : name);
        contractParameter.getRemark().setContent(remark == null ? contractParameter.getRemark().getKey() : remark);
        contractParameter.getHint().setContent(remark == null ? contractParameter.getHint().getKey() : hint);
    }
}
