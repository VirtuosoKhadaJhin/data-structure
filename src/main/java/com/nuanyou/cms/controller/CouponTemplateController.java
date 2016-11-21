package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CouponGroupDao;
import com.nuanyou.cms.dao.CouponTemplateDao;
import com.nuanyou.cms.entity.coupon.CouponGroup;
import com.nuanyou.cms.entity.coupon.CouponTemplate;
import com.nuanyou.cms.entity.enums.UserRange;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CouponTemplateService;
import com.nuanyou.cms.util.BeanUtils;
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
@RequestMapping("couponTemplate")
public class CouponTemplateController {

    @Autowired
    private CouponTemplateDao couponTemplateDao;
    @Autowired
    private CouponGroupDao couponGroupDao;
    @Autowired
    private CouponTemplateService couponTemplateService;


    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        CouponTemplate entity = couponTemplateDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(CouponTemplate entity) {
        couponTemplateService.saveNotNull(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        couponTemplateDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String nameOrId,
                       CouponTemplate entity, Model model) {
        if (StringUtils.isNotBlank(nameOrId)) {
            if (StringUtils.isNumeric(nameOrId)) {
                entity.setId(NumberUtils.toLong(nameOrId));
            } else {
                entity.setTitle(nameOrId);
            }
        }

        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("title", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);

        Page<CouponTemplate> page = couponTemplateDao.findAll(Example.of(entity, matcher), pageable);
        List<CouponGroup> couponGroups = couponGroupDao.findAll();
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        model.addAttribute("couponGroups", couponGroups);
        model.addAttribute("userRanges", UserRange.values());
        model.addAttribute("nameOrId", nameOrId);
        return "couponTemplate/list";
    }

}