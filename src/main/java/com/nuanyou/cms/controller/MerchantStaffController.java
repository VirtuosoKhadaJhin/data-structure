package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantStaffDao;
import com.nuanyou.cms.entity.MerchantStaff;
import com.nuanyou.cms.service.MerchantStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("merchantStaff")
public class MerchantStaffController {

    @Autowired
    private MerchantStaffDao merchantStaffDao;

    @Autowired
    private MerchantStaffService merchantStaffService;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        MerchantStaff entity = merchantStaffDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(MerchantStaff entity) {
        merchantStaffService.saveNotNull(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        merchantStaffDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(MerchantStaff entity, Model model) {
        List<MerchantStaff> list = merchantStaffDao.findAll(Example.of(entity));
        model.addAttribute("list", list);
        model.addAttribute("entity", entity);
        return "merchantStaff/list";
    }

}