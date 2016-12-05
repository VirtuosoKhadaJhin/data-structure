package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.ItemCatDao;
import com.nuanyou.cms.entity.ItemCat;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.service.ItemCatService;
import com.nuanyou.cms.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("itemCat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("add")
    public String add(ItemCat entity) {
        itemCatDao.save(entity);
        return "itemCat/list";
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {    //all merchant infos
        List<Merchant> merchants = merchantService.getIdNameList();
        ItemCat entity = null;
        if (id != null) {
            entity = itemCatDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("merchants", merchants);
        return "itemCat/edit";
    }

    @RequestMapping("update")
    public String update(ItemCat entity, HttpServletResponse response) throws Exception {
        itemCatService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, ItemCat entity, Model model) {
        Page<ItemCat> page = itemCatService.findByCondition(index, entity);
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "itemCat/list";
    }

    @RequestMapping(path = "api/list")
    @ResponseBody
    public APIResult list(Long id) {
        List<ItemCat> list = itemCatDao.findIdNameList(id);
        return new APIResult(list);
    }

}