package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CulturalRecommendationsGroupDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.CulturalRecommendationsGroup;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.CulturalRecommendationsGroupService;
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
@RequestMapping("culturalRecommendationsGroup")
public class CulturalRecommendationsGroupController {

    @Autowired
    private CulturalRecommendationsGroupDao culturalRecommendationsGroupDao;

    @Autowired
    private CulturalRecommendationsGroupService culturalRecommendationsGroupService;

    @Autowired
    private CountryService countryService;


    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            CulturalRecommendationsGroup entity = culturalRecommendationsGroupDao.findOne(id);
            model.addAttribute("entity", entity);
        }
        setEnums(model);
        return "culturalRecommendationsGroup/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        this.edit(id, model);
        model.addAttribute("disabled", true);
        return "culturalRecommendationsGroup/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(CulturalRecommendationsGroup entity, Model model) {
        entity = culturalRecommendationsGroupDao.save(entity);
        model.addAttribute("entity", entity);
        setEnums(model);
        model.addAttribute("disabled", true);
        return "culturalRecommendationsGroup/edit";
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        culturalRecommendationsGroupService.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(CulturalRecommendationsGroup entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        Page<CulturalRecommendationsGroup> page = culturalRecommendationsGroupService.findByCondition(entity,pageable);
        model.addAttribute("page", page);

        setEnums(model);
        model.addAttribute("entity", entity);
        return "culturalRecommendationsGroup/list";
    }

    private void setEnums(Model model) {
        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);
    }
}