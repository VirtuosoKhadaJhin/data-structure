package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.user.ParamsDataMapping;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.LangsDictionaryVo;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractTemplate;
import com.nuanyou.cms.model.contract.request.ParamDetail;
import com.nuanyou.cms.model.contract.request.Template;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    @Autowired
    private ParamsDataMappingService dataMappingService;
    @Autowired
    private LangsDictionaryService dictionaryService;


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
    public String edit(Long id, Model model, Integer optype, HttpServletRequest request) throws UnsupportedEncodingException {

        //获取浏览器当地语言
        List<LangsCountry> langsCountries=getNativeLangs(request);


        //all params
        List<ContractParameter> params=this.contractTemplateService.getAllParams();



        //all countries
        List<Country> countries = this.countryService.getIdNameList();

        //all 所有的数据映射
        List<ParamsDataMapping> dataMappings=this.dataMappingService.findAll();


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
        //selectedIds
        List<ContractParameter> selectedParams = new ArrayList<>();
        List<Long> selectedIds=new ArrayList<>();
        if (optype == 2||optype==3||optype==4) {
            selectedParams = template.getParameters();
            setLangsMessage(selectedParams,request);
            selectedIds=getSeletedIds(selectedParams);
        }




        //setSelectableParams(selectedParams, params);
        model.addAttribute("entity", template);
        model.addAttribute("dataTypeMappings", dataMappings);
        model.addAttribute("selectableParams", params);
        model.addAttribute("selectedParams", selectedParams);
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("selectableLangsCategory", selectableLangsCategory);
        model.addAttribute("countries", countries);
        model.addAttribute("optype", optype);
        model.addAttribute( "langsCountries",langsCountries );
        return "contractTemplate/edit";
    }

    private List<Long> getSeletedIds(List<ContractParameter> selectedParams) {
        List<Long> selectedIds=new ArrayList<>();
        for (ContractParameter selectedParam : selectedParams) {
            selectedIds.add(selectedParam.getId());
        }
        return selectedIds;
    }

    private List<LangsCountry> getNativeLangs(HttpServletRequest request) {
        List<LangsCountry> langsCountries=new ArrayList<>();
        Locale locale = request.getLocale();
        String lang = locale.toLanguageTag();
        LangsCountry langsCountry = LangsCountry.toEnum(lang);
        langsCountries.add(langsCountry);
        if(!langsCountry.equals((LangsCountry.ZH_CN))){
            langsCountries.add(LangsCountry.ZH_CN);
        }  if(!langsCountry.equals((LangsCountry.EN_UK))){
            langsCountries.add(LangsCountry.EN_UK);
        }
        return langsCountries;
    }

    private void setSelectableParams(List<ContractParameter> selectedParams, List<ContractParameter> params) {
        if(selectedParams==null||params==null){
            return;
        }
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
                template.getParamIds(),
                template.getList(),
                template.getTemplateType(),
                template.getTitle(),
                template.getCountryId(),
                template.getId());
    }

    @RequestMapping(value = "getAllParams", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public APIResult getAllParams(
    ) throws IOException {
        return this.contractService.findAllTemplateParameters(1, 100000);

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

    @RequestMapping(path = "discard", method = RequestMethod.GET)
    @ResponseBody
    public APIResult discard(Long id) {
        APIResult res = this.contractService.discardContractTemplate(id);
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        return new APIResult();
    }

    @RequestMapping(path = "delete", method = RequestMethod.GET)
    @ResponseBody
    public APIResult delete(Long id) {
        APIResult res = this.contractService.deleteContract(id);
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


    public void setLangsMessage(List<ContractParameter> langsMessage,HttpServletRequest request) throws UnsupportedEncodingException {
        LangsDictionaryVo dic= dictionaryService.findLangsDictionary("",request.getLocale());
        for (ContractParameter contractParameter : langsMessage) {
            contractParameter.getName().setContent("sdfsdf");
            contractParameter.getRemark().setContent("sdffd");
        }
    }
}