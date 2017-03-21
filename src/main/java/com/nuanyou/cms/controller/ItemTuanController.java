package com.nuanyou.cms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.ItemCatDao;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.ItemTuanDao;
import com.nuanyou.cms.dao.TuanActivityTemplateDao;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.TuanType;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;


@Controller
@RequestMapping("itemTuan")
public class ItemTuanController {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemTuanDao itemTuanDao;

    @Autowired
    private TuanActivityTemplateDao tuanActivityTemplateDao;

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ItemService itemService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);
        Long mchId = merchants.get(0).getId();

        if (id != null) {
            Item entity = itemDao.findOne(id);
            BigDecimal price = itemService.calcItemTuanPrice(id);
            if (price.compareTo(BigDecimal.ZERO) != 0) {//有单品时
                entity.setOkpPrice(price);
                entity.setKpPrice(price);
                entity.setMchPrice(price);
            }

            model.addAttribute("entity", entity);
            mchId = entity.getMerchant().getId();
        }

        List<ItemCat> cats = itemCatDao.findIdNameList(mchId);
        model.addAttribute("cats", cats);

        List<TuanActivityTemplate> templates = tuanActivityTemplateDao.findAll();
        model.addAttribute("templates", templates);

        model.addAttribute("tuanTypes", TuanType.values());
        return "itemTuan/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        this.edit(id, model);
        model.addAttribute("disabled", true);
        return "itemTuan/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(Item entity, String itemTuans, Model model) {
        List<ItemTuan> itemTuanList = null;
        if (StringUtils.isNotBlank(itemTuans)) {
            itemTuanList = JsonUtils.toObj(itemTuans, new TypeReference<List<ItemTuan>>() {
            });
        }

        entity.setItemType(2);
        itemService.saveNotNull(entity, itemTuanList);
        model.addAttribute("entity", entity);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        List<ItemCat> cats = itemCatDao.findIdNameList(entity.getMerchant().getId());
        model.addAttribute("cats", cats);

        List<TuanActivityTemplate> templates = tuanActivityTemplateDao.findAll();
        model.addAttribute("templates", templates);

        model.addAttribute("tuanTypes", TuanType.values());

        model.addAttribute("disabled", true);
        return "redirect:detail?id=" + entity.getId();
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    public String remove(Long id, Model model) {
        itemDao.delete(id);
        return "itemTuan/list";
    }

    @RequestMapping("list")
    public String list(Item entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", startsWith().ignoreCase());

        entity.setItemType(2);
        Page<Item> page = itemDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        model.addAttribute("entity", entity);
        return "itemTuan/list";
    }

    @RequestMapping(path = "api/list")
    @ResponseBody
    public APIResult list(ItemTuan t) {
        List<ItemTuan> sourceList = itemTuanDao.findAll(Example.of(t));
        return new APIResult(sourceList);
    }

}