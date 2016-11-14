package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantSubsidyDao;
import com.nuanyou.cms.entity.MerchantSubsidy;
import com.nuanyou.cms.entity.enums.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("merchantSubsidy")
public class MerchantSubsidyController {

    @Autowired
    private MerchantSubsidyDao merchantSubsidyDao;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        MerchantSubsidy entity = merchantSubsidyDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(MerchantSubsidy entity) {
        merchantSubsidyDao.save(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        merchantSubsidyDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(MerchantSubsidy entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, 10);

        Page<MerchantSubsidy> page = merchantSubsidyDao.findAll(Example.of(entity), pageable);
        model.addAttribute("page", page);

        PeriodType[] periodTypes = PeriodType.values();
        model.addAttribute("periodTypes", periodTypes);

        model.addAttribute("entity", entity);
        return "merchantSubsidy/list";
    }

}