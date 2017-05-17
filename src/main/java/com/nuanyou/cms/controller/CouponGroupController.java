package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CouponGroupDao;
import com.nuanyou.cms.entity.coupon.CouponGroup;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CouponGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("couponGroup")
public class CouponGroupController {

    @Autowired
    private CouponGroupDao couponGroupDao;
    @Autowired
    private CouponGroupService couponGroupService;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        CouponGroup entity = couponGroupDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(CouponGroup entity) {
        couponGroupService.saveNotNull(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {

        couponGroupDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(CouponGroup entity, Model model,@RequestParam(required = false, defaultValue = "1") int index) {
        if (StringUtils.isBlank(entity.getName())) {
            entity.setName(null);
        }
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        Page<CouponGroup> page = couponGroupDao.findAll(Example.of(entity),pageable);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "couponGroup/list";
    }

}