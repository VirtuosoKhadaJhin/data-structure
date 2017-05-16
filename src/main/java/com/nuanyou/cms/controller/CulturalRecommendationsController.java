package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CulturalRecommendationsDao;
import com.nuanyou.cms.entity.CulturalRecommendationsGroup;
import com.nuanyou.cms.entity.CulturalRecommendations;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CulturalRecommendationsGroupService;
import com.nuanyou.cms.service.CulturalRecommendationsService;
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
@RequestMapping("culturalRecommendations")
public class CulturalRecommendationsController {

    @Autowired
    private CulturalRecommendationsDao culturalRecommendationsDao;

    @Autowired
    private CulturalRecommendationsService culturalRecommendationsService;

    @Autowired
    private CulturalRecommendationsGroupService culturalRecommendationsGroupService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(CulturalRecommendations entity, Model model) {
        if (entity.getId() != null) {
            entity = culturalRecommendationsDao.findOne(entity.getId());
        }
        model.addAttribute("entity", entity);
        setEnums(model);
        return "culturalRecommendations/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        CulturalRecommendations entity = culturalRecommendationsDao.findOne(id);
        model.addAttribute("entity", entity);

        setEnums(model);
        model.addAttribute("disabled", true);
        return "culturalRecommendations/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(CulturalRecommendations entity, Model model) {
        entity = culturalRecommendationsDao.save(entity);
        model.addAttribute("entity", entity);
        setEnums(model);
        model.addAttribute("disabled", true);
        return "culturalRecommendations/edit";
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        culturalRecommendationsService.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(CulturalRecommendations entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("title", contains().ignoreCase());

        Page<CulturalRecommendations> page = culturalRecommendationsDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        model.addAttribute("entity", entity);
        return "culturalRecommendations/list";
    }

    private void setEnums(Model model) {
        List<CulturalRecommendationsGroup> culturalRecommendationsCats = culturalRecommendationsGroupService.getIdNameList();
        model.addAttribute("culturalRecommendationsCats", culturalRecommendationsCats);
    }
}