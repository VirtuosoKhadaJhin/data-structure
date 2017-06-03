package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.TemplateParameterDao;
import com.nuanyou.cms.entity.TemplateParameter;
import com.nuanyou.cms.remote.service.RemoteContractService;
import com.nuanyou.cms.service.TemplateParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("templateParameter1")
public class TemplateParameterController {

    @Autowired
    private RemoteContractService contractService;
    @Autowired
    private TemplateParameterService templateParameterService;
    @Autowired
    private TemplateParameterDao templateParameterDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping("add")
    public String add(TemplateParameter templateParameter) {
        templateParameterDao.save(templateParameter);
        return "templateParameter/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        TemplateParameter entity = null;
        if (id != null) {
            entity = templateParameterDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        return "templateParameter/edit";
    }

    @RequestMapping("update")
    public String update(TemplateParameter entity, HttpServletResponse response) throws IOException {
        templateParameterService.saveNotNull(entity);
        String url = "list";
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       TemplateParameter entity, Model model) {


//        APIResult<List<ContractTemplateParameter>> allTemplateParameters = this.contractService.findAllTemplateParameters();
//        List<Sort.Order> orders=new ArrayList<>();
//        orders.add( new Sort.Order(Sort.Direction.ASC, "templateid"));
//        orders.add( new Sort.Order(Sort.Direction.ASC, "sort"));
//        Sort sort=new Sort(orders);
//        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize,sort);
//        Page<TemplateParameter> page = templateParameterService.findByCondition(entity, pageable);
//        model.addAttribute("page", page);
//        model.addAttribute("entity", entity);
//        List<Country> countries = this.countryDao.findAll();
//        model.addAttribute("countries", countries);
        return "contractParameter/list";
    }


}

