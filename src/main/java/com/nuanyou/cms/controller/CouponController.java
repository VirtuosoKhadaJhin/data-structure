package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CouponDao;
import com.nuanyou.cms.entity.coupon.Coupon;
import com.nuanyou.cms.entity.coupon.CouponTemplate;
import com.nuanyou.cms.entity.enums.CouponTemplateType;
import com.nuanyou.cms.entity.enums.PeriodType;
import com.nuanyou.cms.model.CouponBatchVO;
import com.nuanyou.cms.service.CouponTemplateService;
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

import java.util.List;

@Controller
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private CouponTemplateService couponTemplateService;

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    @ResponseBody
    public APIResult detail(Long id) {
        Coupon entity = couponDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @ResponseBody
    public APIResult update(Coupon entity) {
        couponDao.save(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        couponDao.delete(id);
        return new APIResult();
    }

    @RequestMapping("list")
    public String list(Coupon entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, 10);

        Page<Coupon> page = couponDao.findAll(Example.of(entity), pageable);
        model.addAttribute("page", page);

        PeriodType[] periodTypes = PeriodType.values();
        model.addAttribute("periodTypes", periodTypes);

        model.addAttribute("entity", entity);
        return "coupon/list";
    }

    @RequestMapping(value = "batchSendCoupon", method = RequestMethod.GET)
    public String batchSendCoupon(Model model) {
        List<CouponTemplate> merchantTemplate = couponTemplateService.findIdNameList(CouponTemplateType.Merchant);
        List<CouponTemplate> currencyTemplate = couponTemplateService.findIdNameList(CouponTemplateType.Currency);

        model.addAttribute("merchantTemplate", merchantTemplate);
        model.addAttribute("currencyTemplate", currencyTemplate);
        return "coupon/batchSendCoupon";
    }

    @RequestMapping(value = "batchSendCoupon", method = RequestMethod.POST)
    @ResponseBody
    public APIResult batchSendCoupon(CouponBatchVO couponBatch) {
        couponTemplateService.batchSendCoupon(couponBatch);
        return new APIResult<>();
    }

}