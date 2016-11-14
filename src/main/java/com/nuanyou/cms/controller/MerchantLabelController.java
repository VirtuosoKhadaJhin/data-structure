package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantLabelDao;
import com.nuanyou.cms.entity.MerchantLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("merchantLabel")
public class MerchantLabelController {

    @Autowired
    private MerchantLabelDao merchantLabelDao;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        MerchantLabel entity = merchantLabelDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(MerchantLabel entity) {
        Long id = entity.getId();
        if (id == null) {
            merchantLabelDao.save(entity);
        } else {
            MerchantLabel label = merchantLabelDao.findOne(id);
            label.setName(entity.getName());
            label.setKpname(entity.getKpname());
            merchantLabelDao.save(label);
        }
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        merchantLabelDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(MerchantLabel entity, Model model) {
        List<MerchantLabel> list = merchantLabelDao.findAll(Example.of(entity));
        model.addAttribute("list", list);
        model.addAttribute("entity", entity);
        return "merchantLabel/list";
    }

}