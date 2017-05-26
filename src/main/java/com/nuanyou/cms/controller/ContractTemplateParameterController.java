package com.nuanyou.cms.controller;

import com.nuanyou.cms.remote.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Felix on 2017/4/24.
 */

@Controller
@RequestMapping("contractTemplateParameter")
public class ContractTemplateParameterController {
    @Autowired
    private ContractService contractService;


    @RequestMapping("list")
    public String list(Model model,
                       @RequestParam(value = "index", required = false, defaultValue = "1") Integer index,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {


        return "contractTemplateParameter/list";
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {

        return "contractTemplateParameter/edit";
    }


}