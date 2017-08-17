package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.FeatureDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Feature;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.enums.FeatureCat;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;
    @Autowired
    private FeatureDao featureDao;
    @Autowired
    private CountryService countryService;

    @RequestMapping("add")
    public String add(Feature entity) {
        featureDao.save(entity);
        return "feature/list";
    }

    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<Country> countries = countryService.getIdNameList();
        Feature entity = null;
        if (id != null) {
            entity = featureDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        model.addAttribute("cats", FeatureCat.values());
        return "feature/edit";
    }

    @RequestMapping("update")
    public String update(Feature entity, HttpServletResponse response) throws IOException {
        Feature feature = featureService.saveNotNull(entity);
        String url = "edit?type=3&id=" + feature.getId();
        return "redirect:" + url;
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        featureService.delete(id);
        return new APIResult<>();
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Feature entity, Model model) {
        Page<Feature> page = featureService.findByCondition(index, entity);
        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        model.addAttribute("cats", FeatureCat.values());
        return "feature/list";
    }

    @RequestMapping(path = "sort")
    @ResponseBody
    public APIResult setSort(@RequestParam(value = "id") Long id,
                             @RequestParam(value = "top", required = false) Boolean top,
                             @RequestParam(value = "move", required = false) Integer move,
                             @RequestParam(value = "value", required = false) Integer value) {
        Integer sort = featureDao.getSortById(id);
        if (sort == null)
            sort = 1;
        int max = (int) featureDao.count();

        if (value != null) {
            sort = value;
            if (sort < 1)
                sort = 1;
            if (sort > max)
                sort = max;

        } else if (move != null) {
            sort += move;
            if (sort < 1)
                sort = 1;
            if (sort > max)
                sort = max;

        } else if (top != null) {
            sort = top ? 1 : max;

        }
        featureDao.updateSort(id, sort);
        return new APIResult<>();
    }

}