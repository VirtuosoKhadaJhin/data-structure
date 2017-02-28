package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.dao.MerchantHeadimgDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantHeadimg;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantHeadimgService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;


@Controller
@RequestMapping("merchantHeadimg")
public class MerchantHeadimgController {

    @Autowired
    private MerchantHeadimgDao merchantHeadimgDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private MerchantHeadimgService merchantHeadimgService;

    @Autowired
    private MerchantService merchantService;


    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public APIResult add(MerchantHeadimg entity) {
        entity.setSize(1);
        merchantHeadimgService.saveNotNull(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    @ResponseBody
    public APIResult remove(Long id) {
        merchantHeadimgDao.delete(id);
        return new APIResult<>();
    }

    @RequestMapping(path = "top", method = RequestMethod.POST)
    @ResponseBody
    public APIResult top(Long id) {
        merchantHeadimgService.setTop(id);
        return new APIResult<>();
    }

    @RequestMapping("edit")
    public String edit(MerchantHeadimg entity, Model model) {
        List<MerchantHeadimg> list = merchantHeadimgService.find(entity);
        model.addAttribute("list", list);
        model.addAttribute("entity", entity);
        return "merchantHeadimg/edit";
    }

    @RequestMapping(path = "setIndexImgUrl",method = RequestMethod.POST)
    @ResponseBody
    public APIResult setIndexImgUrl(@RequestParam(required = true) String id,@RequestParam String detailImgUrl){



        return new APIResult<>();
    }


    @RequestMapping("list")
    public String list(Merchant entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, 10, Sort.Direction.DESC, "id");

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase()).withMatcher("kpname", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        Page<Merchant> page = merchantDao.findAll(Example.of(entity, matcher), pageable);
        List<Merchant> content = page.getContent();
        for (Merchant merchant : content) {
            List<MerchantHeadimg> list = merchantHeadimgService.find(new MerchantHeadimg(merchant.getId()));
            merchant.setHeadimgs(list);
        }

        model.addAttribute("page", page);

        List<Country> countries = countryDao.findAll();
        model.addAttribute("countries", countries);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        return "merchantHeadimg/list";
    }

}