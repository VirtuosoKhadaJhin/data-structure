package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.DistrictDao;
import com.nuanyou.cms.dao.RateDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.Rate;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.RateService;
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
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("rate")
public class RateController {

    @Autowired
    private RateService rateService;
    @Autowired
    private RateDao rateDao;


    @Autowired
    private DistrictDao districtDao;

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private CountryService countryService;

    @RequestMapping("add")
    public String add(Rate entity) {
        rateDao.save(entity);
        return "rate/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        // all countries
        List<Country> countries = this.countryDao.findAll();
        Rate entity = null;
        if (id != null) {
            entity = rateDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "rate/edit";
    }

    @RequestMapping("update")
    public String update(Rate entity, HttpServletResponse response) throws IOException {

        Country country = countryService.saveNotNull(entity.getCountry());
        rateService.saveNotNull(entity);
        //countryDao.updateCountryRate(entity.getCountry().getId(),entity.getCountry().getRadio());
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index, Rate entity, Model model) {
        Page<Rate> page = rateService.findByCondition(index, entity);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "rate/list";
    }

    @RequestMapping("districtRate")
    public String districtRate(Long id, Model model) {
        List<District> districts = this.districtDao.findByCountryId(id);
        model.addAttribute("districts", districts);
        return "rate/districtRate";
    }

    @RequestMapping("savedistrictRate")
    @ResponseBody
    public APIResult savedistrictRate(Long id, BigDecimal radio) {
        districtDao.updateDistrictRate(id, radio);
        return new APIResult<>(ResultCodes.Success);
    }


}

