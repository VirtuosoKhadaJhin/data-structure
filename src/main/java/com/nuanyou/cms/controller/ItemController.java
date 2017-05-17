package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.ItemSupportType;
import com.nuanyou.cms.entity.enums.TuanType;
import com.nuanyou.cms.model.ItemVO;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.service.MerchantService;
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
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ItemService itemService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList(true);
        model.addAttribute("merchants", merchants);

        if (id != null) {
            Item entity = itemDao.findOne(id);
            model.addAttribute("entity", entity);
        }

        model.addAttribute("tuanTypes", TuanType.values());
        model.addAttribute("supportTypes", ItemSupportType.values());
        return "item/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList(true);
        model.addAttribute("merchants", merchants);

        Item entity = itemDao.findOne(id);
        model.addAttribute("entity", entity);

        model.addAttribute("tuanTypes", TuanType.values());
        model.addAttribute("supportTypes", ItemSupportType.values());

        model.addAttribute("disabled", true);
        return "item/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(ItemVO vo, Model model) {
        vo.setItemType(1);
        Item item = itemService.saveNotNull(vo);
        model.addAttribute("entity", item);

        List<Merchant> merchants = merchantService.getIdNameList(true);
        model.addAttribute("merchants", merchants);

        model.addAttribute("tuanTypes", TuanType.values());
        model.addAttribute("supportTypes", ItemSupportType.values());

        model.addAttribute("disabled", true);
        return "item/edit";
    }

    @RequestMapping("list")
    public String list(Item entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("cat.name", contains().ignoreCase())
                .withMatcher("merchant.name", contains().ignoreCase())
                .withMatcher("name", contains().ignoreCase());

        entity.setItemType(1);
        Page<Item> page = itemDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        model.addAttribute("entity", entity);
        return "item/list";
    }

    @RequestMapping(path = "api/list")
    @ResponseBody
    public APIResult list(Item item) {
        List<Item> sourceList = itemDao.findAll(Example.of(item));
        return new APIResult(sourceList);
    }

}