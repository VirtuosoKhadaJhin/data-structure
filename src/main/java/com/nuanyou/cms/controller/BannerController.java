package com.nuanyou.cms.controller;

import com.nuanyou.cms.dao.BannerDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Banner;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.service.BannerService;
import com.nuanyou.cms.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;
    @Autowired
    private BannerDao bannerDao;
    @Autowired
    private CountryService countryService;

    @RequestMapping("add")
    public String add(Banner entity) {
        bannerDao.save(entity);
        return "banner/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<Country> countries = countryService.getIdNameList();
        Banner entity = null;
        if (id != null) {
            entity = bannerDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "banner/edit";
    }

    @RequestMapping("update")
    public String update(Banner entity, HttpServletResponse response) throws IOException {
        bannerService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @Autowired
    protected HttpServletRequest request;
    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Banner entity, Model model) {
        List<Country> countries = countryService.getIdNameList();
        Page<Banner> page = bannerService.findByCondition(index, entity);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("countries", countries);
        return "banner/list";
    }


}

