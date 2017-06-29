package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.DistrictDao;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.model.DistrictVo;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.service.DistrictService;
import com.nuanyou.cms.service.LangsCategoryService;
import com.nuanyou.cms.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("district")
public class DistrictController {
    @Autowired
    private LangsCategoryService categoryService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private DistrictDao districtDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping("add")
    public String add(District district) {
        districtDao.save(district);
        return "district/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type, HttpServletRequest request) {
        // all countries
        List<Country> countries = this.countryDao.findAll();
        District entity = null;
        if (id != null) {
            entity = districtDao.findOne(id);
        }
        //本地语言
        List<LangsCountry> langsCountries = getNativeLangs(request);

        // 语言分类, 用于增加多语言弹窗里面的select下拉列表
        List<LangsCategory> categories = categoryService.findAllCategories();

        model.addAttribute("langsCountries", langsCountries);
        model.addAttribute("categories", categories);
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "district/edit";
    }

    @RequestMapping("update")
    public String update(DistrictVo districtVo, HttpServletResponse response) throws IOException {
        districtService.updateDistrict(districtVo);
        return "redirect:list";
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String nameOrId,
                       @RequestParam(required = false, defaultValue = "id") String propertie,
                       @RequestParam(required = false) Sort.Direction direction,
                       District entity, Model model,
                       HttpServletRequest request) {

        if (StringUtils.isNotBlank(nameOrId)) {
            if (StringUtils.isNumeric(nameOrId)) {
                entity.setId(NumberUtils.toLong(nameOrId));
            } else {
                entity.setName(nameOrId);
            }
        }

        Pageable pageable;
        if (direction == null)
            pageable = new PageRequest(index - 1, PageUtil.pageSize);
        else
            pageable = new PageRequest(index - 1, PageUtil.pageSize, direction, propertie);

        Locale locale = request.getLocale();
        Page<DistrictVo> page = districtService.findByCondition(entity, pageable, locale);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("flag", "district");
        model.addAttribute("nameOrId", nameOrId);

        List<Country> countries = this.countryDao.findAll();
        model.addAttribute("countries", countries);
        return "district/list";
    }


    @RequestMapping("api/getDistricts")
    @ResponseBody
    public APIResult getDistricts(City city) {
        List<District> list = districtDao.findIdNameListByCityId(city.getId());
        return new APIResult(list);
    }

    @RequestMapping("api/list")
    @ResponseBody
    public APIResult list(Long id) {
        List<District> list = districtDao.findIdNameList(id);
        return new APIResult(list);
    }

    private List<LangsCountry> getNativeLangs(HttpServletRequest request) {
        List<LangsCountry> langsCountries = new ArrayList<>();
        Locale locale = request.getLocale();
        String lang = locale.toLanguageTag();
        LangsCountry langsCountry = LangsCountry.toEnum(lang);
        langsCountries.add(langsCountry);
        if (!langsCountry.equals((LangsCountry.ZH_CN))) {
            langsCountries.add(LangsCountry.ZH_CN);
        }
        if (!langsCountry.equals((LangsCountry.EN_UK))) {
            langsCountries.add(LangsCountry.EN_UK);
        }
        return langsCountries;
    }

}

