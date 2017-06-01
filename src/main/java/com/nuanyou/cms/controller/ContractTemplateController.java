package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractParameters;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequests;
import com.nuanyou.cms.remote.ContractService;
import com.nuanyou.cms.service.ContractTemplateService;
import com.nuanyou.cms.service.CountryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private CountryService countryService;

    @Autowired
    private ContractTemplateService contractTemplateService;

    @RequestMapping("list")
    public String list(Model model,
                       @RequestParam(value = "countryId", required = false) Long countryId,
                       @RequestParam(value = "status", required = false) TemplateStatus status,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        List<Country> countries = this.countryService.getIdNameList();
        List<TemplateStatus> templateStatuses = Arrays.asList(TemplateStatus.values());
        Page<Contract> page = this.contractTemplateService.findContractTemplateList(countryId, status == null ? null : status.getValue(), type, index, limit);
        model.addAttribute("page", page);
        model.addAttribute("countries", countries);
        model.addAttribute("templateStatuses", templateStatuses);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("countryId", countryId);
        return "contractTemplate/list";
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

            if (param.isCommon()) {
                commonParams.add(param);
            }
        }

        //all countries
        List<Country> countries = this.countryService.getIdNameList();


        //vo info
        ContractTemplate template = null;
        if (id != null) {
            APIResult<ContractTemplate> contractConfig = this.contractService.getContractConfig(id);
            if (contractConfig.getCode() != 0) {
                throw new APIException(contractConfig.getCode(), contractConfig.getMsg());
            }
            template = contractConfig.getData();
        }


        model.addAttribute("allParams", params);
        model.addAttribute("countries", countries);
        model.addAttribute("entity", template);
        if(type==1){
            model.addAttribute("selectedParams", commonParams);
        }else{
            model.addAttribute("selectedParams", template.getParameters());
        }
        model.addAttribute("type", type);
        return "contractTemplate/edit";
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
        return this.contractTemplateService.saveTemplate(selectedParamIds, templateParameterRequests, templateType, title, countryId, id);
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











    @RequestMapping(path = "publish", method = RequestMethod.GET)
    @ResponseBody
    public APIResult publish(Long id) {
        APIResult res = this.contractService.releaseContractTemplate(id);
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        return new APIResult();
    }


    @InitBinder("params")
    public void initAccountBinder(WebDataBinder binder) {
        // @ModelAttribute("params")
        binder.setFieldDefaultPrefix("params.");
    }


}