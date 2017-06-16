package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractParameters;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequest;
import com.nuanyou.cms.model.contract.request.UpdateParameterRequest;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.LangsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contractParameter")
public class ContractParameterController {

    @Autowired
    private RemoteContractService contractService;

    @Autowired
    private LangsCategoryService categoryService;

    @RequestMapping("list")
    public String list(Model model,
                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        APIResult<ContractParameters> allTemplateParameters = contractService.findAllTemplateParameters(index, limit);
        ContractParameters data = allTemplateParameters.getData();
        Pageable pageable = new PageRequest(index - 1, limit);
        List<ContractParameter> list = data.getList();
        Page<Contract> page = new PageImpl(list, pageable, data.getTotal());
        model.addAttribute("page", page);
        return "contractParameter/list";
    }


    @RequestMapping("update")
    public String update(HttpServletResponse response, TemplateParameterRequest request, Long id, Integer dataType) throws IOException {
        request.setType(dataType);
        if (id == null) {
            APIResult<ContractParameter> res = this.contractService.saveTemplateParamter(request);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            ContractParameter data = res.getData();
            String url = "edit?type=3&id=" + data.getId();
            return "redirect:" + url;
        } else {
            UpdateParameterRequest updateParameterRequest = new UpdateParameterRequest();
            updateParameterRequest.setHint(request.getHint());
            updateParameterRequest.setRegex(request.getRegex());
            updateParameterRequest.setName(request.getName());
            updateParameterRequest.setRemark(request.getRemark());
            updateParameterRequest.setNote(request.getNote());
            APIResult<ContractParameter> res = this.contractService.updateContractParameter(id, updateParameterRequest);
            if (res.getCode() != 0) {
                throw new APIException(res.getCode(), res.getMsg());
            }
            String url = "edit?type=3&id=" + id;
            return "redirect:" + url;
        }
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    @ResponseBody
    public APIResult remove(Long id) {
        APIResult res = this.contractService.deleteContractParameter(id);
        if (res.getCode() != 0) {
            throw new APIException(res.getCode(), res.getMsg());
        }
        return new APIResult();
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, Long id, Model model, Integer type) {
        ContractParameter data = null;
        if (id != null) {
            APIResult<ContractParameter> res = this.contractService.saveTemplateParamter(id);
            data = res.getData();
        }

        //本地语言
        List<LangsCountry> langsCountries = getNativeLangs(request);

        // 语言分类, 用于增加多语言弹窗里面的select下拉列表
        List<LangsCategory> categories = categoryService.findAllCategories();

        model.addAttribute("langsCountries", langsCountries);
        model.addAttribute("categories", categories);
        model.addAttribute("entity", data);
        model.addAttribute("type", type);
        return "contractParameter/edit";
    }

    private List<LangsCountry> getNativeLangs(HttpServletRequest request) {
        List<LangsCountry> langsCountries = new ArrayList<>();
        Locale locale = request.getLocale();
        String lang = locale.toLanguageTag();
        LangsCountry langsCountry = LangsCountry.toEnum(lang);
        langsCountries.add(langsCountry);
        if (!langsCountry.equals((LangsCountry.ZH_CN))) {
            langsCountries.add(LangsCountry.ZH_CN);
        }
        if (!langsCountry.equals((LangsCountry.EN_UK))) {
            langsCountries.add(LangsCountry.EN_UK);
        }
        return langsCountries;
    }


}