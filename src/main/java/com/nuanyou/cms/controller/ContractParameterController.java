package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.entity.TemplateParameter;
import com.nuanyou.cms.model.contract.output.Contract;
import com.nuanyou.cms.model.contract.output.ContractParameter;
import com.nuanyou.cms.model.contract.output.ContractParameters;
import com.nuanyou.cms.model.contract.request.TemplateParameterRequest;
import com.nuanyou.cms.remote.ContractService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contractParameter")
public class ContractParameterController {

    @Autowired
    private ContractService contractService;


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
    public String update(TemplateParameter entity, HttpServletResponse response, TemplateParameterRequest request) throws IOException {
        APIResult<ContractParameter> contractParameterAPIResult = this.contractService.saveTemplateParamter(request);
        String url = "list";
        return "redirect:" + url;
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        TemplateParameterRequest entity = new TemplateParameterRequest();
        entity.setKey("1");
        entity.setDefaultValue("2");
        entity.setEditable(true);
        entity.setHint("3");
        entity.setRegex("4");
        entity.setNullable(true);
        entity.setMultiValuable(true);
        entity.setSort(3);
        entity.setName("5");
        entity.setType(2);
        //entity = cityDao.findOne(id);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "contractParameter/edit";
    }


}