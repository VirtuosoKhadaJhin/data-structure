package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.DiscountDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.Discount;
import com.nuanyou.cms.entity.enums.DiscountType;
import com.nuanyou.cms.model.DiscountQueryParam;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.DiscountService;
import com.nuanyou.cms.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("discount")
public class DiscountController {

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private CountryService countryService;

    @Autowired
    private DiscountService discountService;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Discount entity = discountDao.findOne(id);
            model.addAttribute("entity", entity);
        }

        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);

        model.addAttribute("discountTypes", DiscountType.values());
        return "discount/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        Discount entity = discountDao.findOne(id);
        model.addAttribute("entity", entity);

        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);

        model.addAttribute("discountTypes", DiscountType.values());

        model.addAttribute("disabled", true);
        return "discount/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(Discount entity, Model model) {
        entity = discountService.saveNotNull(entity);
        model.addAttribute("entity", entity);

        String url = "detail?id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    @ResponseBody
    public APIResult remove(Long id) {
        discountService.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String nameOrId,
                       Discount entity, Model model) {


        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");

        DiscountQueryParam param = new DiscountQueryParam ();
        if (StringUtils.isNotBlank(nameOrId)) {
            if (StringUtils.isNumeric(nameOrId)) {
                param.id = NumberUtils.toLong(nameOrId);
            } else {
                param.name = nameOrId;
            }
        }
        param.cityId = entity.getCity() != null ? entity.getCity().getId() :null;
        param.countryId = entity.getCountry() != null ? entity.getCountry().getId() :null;
        param.type = entity.getType() != null ? entity.getType().getValue() :null;
        Page<Discount> page = discountService.findDiscount( param,  pageable);
        model.addAttribute("page", page);

        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);

        model.addAttribute("discountTypes", DiscountType.values());

        model.addAttribute("nameOrId", nameOrId);
        model.addAttribute("entity", entity);
        return "discount/list";
    }

}