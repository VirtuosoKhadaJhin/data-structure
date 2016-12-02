package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.DistrictDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.DistrictService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("district")
public class DistrictController {

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
    public String edit(Long id, Model model, Integer type) {
        // all countries
        List<Country> countries = this.countryDao.findAll();
        District entity = null;
        if (id != null) {
            entity = districtDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("type", type);
        model.addAttribute("countries", countries);
        return "district/edit";
    }

    @RequestMapping("update")
    public String update(District entity, HttpServletResponse response) throws IOException {
        districtService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String nameOrId,
                       @RequestParam(required = false, defaultValue = "id") String propertie,
                       @RequestParam(required = false) Sort.Direction direction,
                       District entity, Model model) {

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

        Page<District> page = districtService.findByCondition(entity, pageable);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("flag", "district");
        model.addAttribute("nameOrId", nameOrId);

        List<Country> countries = this.countryDao.findAll();
        model.addAttribute("countries", countries);
        return "district/list";
    }


    @RequestMapping("api/list")
    @ResponseBody
    public APIResult list(Long id) {
        List<District> list = districtDao.findIdNameList(id);
        return new APIResult(list);
    }

}

