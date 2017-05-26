package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.contract.enums.TemplateStatus;
import com.nuanyou.cms.model.contract.output.*;
import com.nuanyou.cms.model.contract.request.BatchTemplateParameterRequest;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequest;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequests;
import com.nuanyou.cms.model.contract.request.TemplateRequest;
import com.nuanyou.cms.remote.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
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
        Page<Contract> page = new PageImpl(list, pageable, data.getTotal());
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

        //all countries
        List<Country> countries = this.countryDao.findAll();


        //vo info
        ContractTemplate template=null;

        List<Long> selectedIds=null;
        if(id!=null){
            APIResult<ContractTemplate> contractConfig = this.contractService.getContractConfig(id);
            if (contractConfig.getCode() != 0) {
                throw new APIException(contractConfig.getCode(), contractConfig.getMsg());
            }
            template = contractConfig.getData();

            //vo selectedIds
            selectedIds=getSelectedIds(template.getParameters());
        }



        model.addAttribute("params", params);
        model.addAttribute("countries", countries);
        model.addAttribute("entity", template);
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("type", type);
        return "contractTemplate/edit";
    }

    private List<Long> getSelectedIds(List<ContractParameter> parameters) {
        List<Long> selectedIds=new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            selectedIds.add(parameters.get(i).getId());
        }
        return selectedIds;
    }


    @RequestMapping("update")
    public String update(HttpServletResponse response,
                         Long[] selectedParamIds,
                         TemplateParameterRequests templateParameterRequests,
                         Integer type,
                         String title,
                         Long countryId
    ) throws IOException {
        List<TemplateParameterRequest> list = toListTemplateParameter(templateParameterRequests);
        BatchTemplateParameterRequest batch = new BatchTemplateParameterRequest();
        batch.setParameterRequests(list);
        APIResult<List<Long>> idList = this.contractService.saveTemplateParamters(batch);
        if (idList.getCode() != 0) {
            throw new APIException(idList.getCode(), idList.getMsg());
        }
        List<Long> idsSum = idList.getData();
        idsSum.addAll(Arrays.asList(selectedParamIds));
        TemplateRequest templateRequest = new TemplateRequest();
        templateRequest.setParamterids(idsSum);
        templateRequest.setType(type);
        templateRequest.setCountryId(countryId);
        templateRequest.setTitle(title);
        APIResult<ContractTemplate> contractTemplateAPIResult = this.contractService.saveTemplate(templateRequest);
        if (contractTemplateAPIResult.getCode() != 0) {
            throw new APIException(contractTemplateAPIResult.getCode(), contractTemplateAPIResult.getMsg());
        }
        String url = "edit?type=3&id=" + contractTemplateAPIResult.getData().getId();
        return "redirect:" + url;
    }

    private List<TemplateParameterRequest> toListTemplateParameter(TemplateParameterRequests templateParameterRequests) {
        Integer size = templateParameterRequests.getKey().length;
        List<TemplateParameterRequest> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TemplateParameterRequest request = new TemplateParameterRequest();
            request.setName(templateParameterRequests.getName()[i]);
            request.setType(templateParameterRequests.getType()[i]);
            request.setMultiValuable(templateParameterRequests.getMultiValuable()[i]);
            request.setEditable(templateParameterRequests.getEditable()[i]);
            request.setDefaultValue(templateParameterRequests.getHint()[i]);
            request.setKey(templateParameterRequests.getKey()[i]);
            request.setNullable(templateParameterRequests.getNullable()[i]);
            request.setRegex(templateParameterRequests.getRegex()[i]);
            request.setHint(templateParameterRequests.getHint()[i]);
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