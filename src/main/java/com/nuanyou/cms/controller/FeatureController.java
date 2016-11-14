package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.FeatureDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Feature;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    private CountryDao countryDao;

    @RequestMapping("add")
    public String add(Feature entity) {
        featureDao.save(entity);
        return "feature/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<Country> countries = this.countryDao.findAll();
        Feature entity = null;
        if (id != null) {
            entity = featureDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "feature/edit";
    }

    @RequestMapping("update")
    public String update(Feature entity, HttpServletResponse response) throws IOException {
        Feature feature = featureService.saveNotNull(entity);
        String url = "edit?type=3&id=" + feature.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Feature entity, Model model) {
        Page<Feature> page = featureService.findByCondition(index, entity);
        List<Country> countries = this.countryDao.findAll();
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        return "feature/list";
    }


}

