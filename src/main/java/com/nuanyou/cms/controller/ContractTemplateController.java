package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.*;
import com.nuanyou.cms.model.contract.request.*;
import com.nuanyou.cms.remote.ContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contractTemplate")
public class ContractTemplateController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private CountryDao countryDao;


    @RequestMapping("list")
    public String list(Model model,
                       @RequestParam(value = "countryId", required = false) Long countryId,
                       @RequestParam(value = "status", required = false) TemplateStatus status,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        List<Country> countries = this.countryDao.findAll();
        List<TemplateStatus> templateStatuses = Arrays.asList(TemplateStatus.values());
        APIResult<ContractTemplates> api = this.contractService.findContractTemplateList(countryId, status == null ? null : status.getValue(), type, index, limit);
        ContractTemplates data = api.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<ContractTemplate> list = data.getList();
        setCountryName(list);
        Page<Contract> page = new PageImpl(list, pageable, data.getTotal());
        model.addAttribute("page", page);
        model.addAttribute("countries", countries);
        model.addAttribute("templateStatuses", templateStatuses);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("countryId", countryId);
        return "contractTemplate/list";
    }

    private void setCountryName(List<ContractTemplate> list) {
        for (int i = 0; i < list.size(); i++) {
            ContractTemplate contractTemplate = list.get(i);
            if(contractTemplate!=null){
                if(contractTemplate.getCountryId()!=null){
                    Country one = countryDao.findOne(list.get(i).getCountryId());
                    contractTemplate.setCountryName(one.getName());
                }
            }
        }
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {

        //all params
        APIResult<ContractParameters> allTemplateParameters = this.contractService.findAllTemplateParameters(1, 100000);
        if (allTemplateParameters.getCode() != 0) {
            throw new APIException(allTemplateParameters.getCode(), allTemplateParameters.getMsg());
        }
        ContractParameters data = allTemplateParameters.getData();
        List<ContractParameter> params = data.getList();


        //all common params
        List<ContractParameter> commonParams = new ArrayList<>();
        for (ContractParameter param : params) {

            if(param.isCommon()){
                commonParams.add(param);
            }
        }

        //all countries
        List<Country> countries = this.countryDao.findAll();


        //vo info
        ContractTemplate template = null;

        List<Long> selectedIds = new ArrayList<>();
        if (id != null) {
            APIResult<ContractTemplate> contractConfig = this.contractService.getContractConfig(id);
            if (contractConfig.getCode() != 0) {
                throw new APIException(contractConfig.getCode(), contractConfig.getMsg());
            }
            template = contractConfig.getData();

            //vo selectedIds
            selectedIds = getSelectedIds(template.getParameters());
        }


        model.addAttribute("params", params);
        model.addAttribute("countries", countries);
        model.addAttribute("entity", template);
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("commonParams", commonParams);
        model.addAttribute("type", type);
        return "contractTemplate/edit";
    }

    private List<Long> getSelectedIds(List<ContractParameter> parameters) {
        List<Long> selectedIds = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            selectedIds.add(parameters.get(i).getId());
        }
        return selectedIds;
    }

    @RequestMapping(value = "saveTemplate", method = RequestMethod.POST)
    @ResponseBody
    public APIResult saveTemplate(Long[] selectedParamIds,
                                      TemplateParameterRequests templateParameterRequests,
                                      Integer templateType,
                                      String title,
                                      Long countryId,
                                      Integer type,
                                      Long id

    ) throws IOException {
        validate(selectedParamIds, templateParameterRequests, templateType, title, countryId);
        APIResult res= save(selectedParamIds, templateParameterRequests, templateType, title, countryId, id);
        return res;
    }

    private void validate(Long[] selectedParamIds, TemplateParameterRequests templateParameterRequests, Integer type, String title, Long countryId) {
        if (type == null) {
            throw new APIException(ResultCodes.Fail, "类型不能为空");
        } else if (StringUtils.isEmpty(title)) {
            throw new APIException(ResultCodes.Fail, "title不能为空");
        } else if (countryId == null) {
            throw new APIException(ResultCodes.Fail, "国家不能为空");
        }
        boolean res = templateParameterRequests.validateTemplate();
        if (res == false) {
            throw new APIException(ResultCodes.Fail, "模板参数key name defaultvalue 不能有空");
        }
        boolean validateKeys = templateParameterRequests.validateKeys();
        if (validateKeys == false) {
            throw new APIException(ResultCodes.Fail, "模板key不能重复");
        }
    }


    public APIResult save(
                       Long[] selectedParamIds,
                       TemplateParameterRequests templateParameterRequests,
                       Integer templateType,
                       String title,
                       Long countryId,
                       Long id
    ) throws IOException {
        //fetch param ids
        List<TemplateParameterRequest> list = toListTemplateParameter(templateParameterRequests);
        BatchTemplateParameterRequest batch = new BatchTemplateParameterRequest();
        batch.setParameterRequests(list);
        APIResult<List<Long>> idList = this.contractService.saveTemplateParamters(batch);
        if (idList.getCode() != 0) {
            throw new APIException(idList.getCode(), idList.getMsg());
        }
        List<Long> idsSum = idList.getData();//ids from added param ids
      if(selectedParamIds!=null&&selectedParamIds.length>0){
          idsSum.addAll(Arrays.asList(selectedParamIds));//ids from selectedParam ids
      }

        List<Long> originalParamIds=templateParameterRequests.getParamId();
        if(originalParamIds!=null&&!originalParamIds.isEmpty()){
            idsSum.addAll(originalParamIds);//ids from original param ids
        }
        idsSum=removeSame(idsSum);


        Long newVersionId=null;
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
            newVersionId=res.getData().getId();
        } else {
            //update template
            TemplateUpdateRequest request = new TemplateUpdateRequest();
            request.setTitle(title);
            request.setParamterids(idsSum);
            APIResult<ContractTemplate> res = this.contractService.updateTemplate(id, request);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            newVersionId=res.getData().getId();
        }
        return new APIResult(newVersionId);


    }

    private List<Long> removeSame(List<Long> idsSum) {
        HashSet<Long> longs = new HashSet<>(idsSum);
        longs.remove(null);
        return new ArrayList<>(longs);

    }



    private List<TemplateParameterRequest> toListTemplateParameter(TemplateParameterRequests templateParameterRequests) {
        if(templateParameterRequests.getKey()==null){
            return new ArrayList<>();

        }
        Integer size = templateParameterRequests.getKey().size();
        List<TemplateParameterRequest> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TemplateParameterRequest request = new TemplateParameterRequest();
            if(templateParameterRequests.getParamId().get(i)!=null){
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

    @InitBinder("params")
    public void initAccountBinder(WebDataBinder binder) {
        // @ModelAttribute("params")
        binder.setFieldDefaultPrefix("params.");
    }



}