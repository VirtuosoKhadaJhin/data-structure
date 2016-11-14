package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.MerchantHeadimgOldDao;
import com.nuanyou.cms.entity.MerchantHeadimgOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("merchantHeadimgOld")
public class MerchantHeadimgOldController {

    @Autowired
    private MerchantHeadimgOldDao merchantHeadimgOldDao;


    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public APIResult add(MerchantHeadimgOld entity) {
        merchantHeadimgOldDao.save(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    @ResponseBody
    public APIResult remove(Long id, Model model) {
        merchantHeadimgOldDao.delete(id);
        return new APIResult<>();
    }

    @RequestMapping("list")
    public String list(MerchantHeadimgOld entity, Model model) {
        List<MerchantHeadimgOld> list = merchantHeadimgOldDao.findAll(Example.of(entity));
        model.addAttribute("list", list);
        model.addAttribute("entity", entity);
        return "merchantHeadimgOld/list";
    }

}