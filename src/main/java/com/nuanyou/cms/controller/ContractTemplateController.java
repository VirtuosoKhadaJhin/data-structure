package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractParameters;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.request.ParamDetail;
import com.nuanyou.cms.model.contract.request.Template;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.ContractTemplateService;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.LangsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contractTemplate")
public class ContractTemplateController {

    @Autowired
    private RemoteContractService contractService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private LangsCategoryService categoryService;

    @RequestMapping("list")
    public String list(Model model,
                       @RequestParam(value = "countryId", required = false) Long countryId,
                       @RequestParam(value = "status", required = false) TemplateStatus status,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        List<Country> countries = this.countryService.getIdNameList();
        List<TemplateStatus> templateStatuses = Arrays.asList(TemplateStatus.values());
        Page<ContractTemplate> page = this.contractTemplateService.findContractTemplateList(countryId, status == null ? null : status.getValue(), type, index, limit);
        model.addAttribute("page", page);
        model.addAttribute("countries", countries);
        model.addAttribute("templateStatuses", templateStatuses);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("countryId", countryId);
        return "contractTemplate/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer optype) {

        //all params
        APIResult<ContractParameters> allTemplateParameters = this.contractService.findAllTemplateParameters(1, 100000);
        if (allTemplateParameters.getCode() != 0) {
            throw new APIException(allTemplateParameters.getCode(), allTemplateParameters.getMsg());
        }
        ContractParameters data = allTemplateParameters.getData();
        List<ContractParameter> params = data.getList();


        //all countries
        List<Country> countries = this.countryService.getIdNameList();


        //add selectable langs
        LangsCategory example=new LangsCategory();
        example.setIndex(1);example.setSize(100000);
        Page<LangsCategory> selectableLangsCategory = this.categoryService.findAllCategories(example);


        //vo info
        ContractTemplate template = null;
        if (id != null) {
            APIResult<ContractTemplate> contractConfig = this.contractService.getContractConfig(id);
            if (contractConfig.getCode() != 0) {
                throw new APIException(contractConfig.getCode(), contractConfig.getMsg());
            }
            template = contractConfig.getData();
        }

        List<ContractParameter> selectedParams = null;
        if (optype == 1) {
            //selectedParams = commonParams;
        } else {
            selectedParams = template.getParameters();
        }

        setSelectableParams(selectedParams, params);
        model.addAttribute("entity", template);
        model.addAttribute("selectableParams", params);
        model.addAttribute("selectedParams", selectedParams);
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);
        model.addAttribute("countries", countries);
        model.addAttribute("optype", optype);
        model.addAttribute ( "langsCountries", LangsCountry.values () );
        return "contractTemplate/edit";
    }

    private void setSelectableParams(List<ContractParameter> selectedParams, List<ContractParameter> params) {
        if(selectedParams==null||params==null){
            return;
        }
        //用迭代器也可以
        for (int i = params.size() - 1; i >= 0; i--) {
            for (int j = 0; j<=selectedParams.size()-1 ; j++) {
                if (params.get(i).getId().equals(selectedParams.get(j).getId())) {
                    params.remove(i);
                    break;
                }
            }
        }
    }


    @RequestMapping(value = "saveTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult saveTemplate(
            @RequestBody Template template
    ) throws IOException {

        return this.contractTemplateService.saveTemplate(
                template.getSelectedParamIds(),
                template.getParamIds(),
                template.getList(),
                template.getTemplateType(),
                template.getTitle(),
                template.getCountryId(),
                template.getId());
    }



    @RequestMapping(value = "getParameterDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult getParameterDetail(
            @RequestBody ParamDetail detail ) throws IOException {
        APIResult<ContractParameter> res = this.contractService.saveTemplateParamter(detail.getSelectedParamId());
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }

        return new APIResult(res.getData());
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