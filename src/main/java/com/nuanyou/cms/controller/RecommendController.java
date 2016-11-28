package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.RecommendDao;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.Recommend;
import com.nuanyou.cms.entity.RecommendCat;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.RecommendCatService;
import com.nuanyou.cms.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Controller
@RequestMapping("recommend")
public class RecommendController {

    @Autowired
    private RecommendDao recommendDao;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendCatService recommendCatService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private MerchantService merchantService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(Recommend entity, Model model) {
        if (entity.getId() != null) {
            entity = recommendDao.findOne(entity.getId());
        }
        model.addAttribute("entity", entity);
        setEnums(model);
        return "recommend/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        Recommend entity = recommendDao.findOne(id);
        model.addAttribute("entity", entity);

        setEnums(model);
        model.addAttribute("disabled", true);
        return "recommend/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(Recommend entity, Model model) {
        entity = recommendDao.save(entity);
        model.addAttribute("entity", entity);
        setEnums(model);
        model.addAttribute("disabled", true);
        return "recommend/edit";
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        recommendService.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(Recommend entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", startsWith().ignoreCase());

        Page<Recommend> page = recommendDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        model.addAttribute("entity", entity);
        return "recommend/list";
    }


    @RequestMapping(path = "api/target/list")
    @ResponseBody
    public APIResult targetList(@RequestParam("id") Integer type) {
        APIResult result = null;
        if (type == 1) {
            List<Merchant> merchants = merchantService.getIdNameList();
            result = new APIResult(merchants);
        } else {
            List<Item> items = itemService.getIdNameList();
            result = new APIResult(items);
        }
        return result;
    }

    private void setEnums(Model model) {
        List<RecommendCat> recommendCats = recommendCatService.getIdNameList();
        model.addAttribute("recommendCats", recommendCats);
    }
}