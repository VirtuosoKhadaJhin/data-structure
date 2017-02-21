package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.ItemDetailimgDao;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.ItemDetailimg;
import com.nuanyou.cms.service.ItemDetailimgService;
import com.nuanyou.cms.service.ItemService;
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
@RequestMapping("itemDetailimg")
public class ItemDetailimgController {

    @Autowired
    private ItemDetailimgDao itemDetailimgDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemDetailimgService itemDetailimgService;

    @Autowired
    private ItemService itemService;


    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public APIResult add(ItemDetailimg entity) {
        itemDetailimgService.saveNotNull(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    @ResponseBody
    public APIResult remove(Long id) {
        itemDetailimgDao.delete(id);
        return new APIResult<>();
    }

    @RequestMapping(path = "top", method = RequestMethod.POST)
    @ResponseBody
    public APIResult top(Long id) {
        itemDetailimgService.setTop(id);
        return new APIResult<>();
    }

    @RequestMapping("edit")
    public String edit(ItemDetailimg entity, Model model) {
        List<ItemDetailimg> list = itemDetailimgService.find(entity);
        model.addAttribute("list", list);
        model.addAttribute("entity", entity);
        return "itemDetailimg/edit";
    }

    @RequestMapping("list")
    public String list(Item entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, 10, Sort.Direction.DESC, "id");

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase()).withMatcher("kpname", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        Page<Item> page = itemDao.findAll(Example.of(entity, matcher), pageable);
        List<Item> content = page.getContent();
        for (Item item : content) {
            List<ItemDetailimg> list = itemDetailimgService.find(new ItemDetailimg(item.getId()));
            item.setDetailimgs(list);
        }

        model.addAttribute("entity", entity);
        model.addAttribute("page", page);
        return "itemDetailimg/list";
    }

}