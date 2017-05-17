package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("rank")
public class RankController {

    @Autowired
    private RankService rankService;
    @Autowired
    private RankDao rankDao;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private MerchantCatDao merchantCatDao;
    @Autowired
    private ItemService itemService;

    @RequestMapping("add")
    public String add(Rank entity) {
        rankDao.save(entity);
        return "rank/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<MerchantCat> cats = this.merchantCatDao.getIdNameList();
        List<Item> tuans = this.itemService.findItemTuans();
        List<Country> countries = this.countryDao.findAll();
        Rank entity = null;
        if (id != null) {
            entity = rankDao.findOne(id);

            setExtra(entity);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        model.addAttribute("cats", cats);
        model.addAttribute("tuans", tuans);
        return "rank/edit";
    }

    @RequestMapping("update")
    public String update(Rank entity) throws IOException {
        rankDao.save(entity);
        return "redirect:edit?type=3&id=" + entity.getId();
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Rank entity, Model model) {
        Page<Rank> page = rankService.findByCondition(index, entity);
        List<Country> countries = this.countryDao.findAll();

        for (Rank rank : page) {
            setExtra(rank);

        }
        model.addAttribute("countries", countries);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "rank/list";
    }

    private void setExtra(Rank rank) {
        rank.setCountry(rank.getCity().getCountry());
        if (rank.getObjtype() == 1) {
            rank.setMerchant(this.merchantDao.getOne(rank.getObjid()));
        } else if (rank.getObjtype() == 2) {
            rank.setItem(this.itemDao.getOne(rank.getObjid()));
        }
    }


}