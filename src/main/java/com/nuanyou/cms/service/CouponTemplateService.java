package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.coupon.CouponTemplate;
import com.nuanyou.cms.entity.enums.CouponTemplateType;
import com.nuanyou.cms.model.CouponBatchVO;
import com.nuanyou.cms.model.CouponTemplateVO;

import java.util.List;

/**
 * Created by Felix on 2016/10/13.
 */
public interface CouponTemplateService {

    CouponTemplateVO saveNotNull(CouponTemplateVO entity);

    List<CouponTemplate> findIdNameList(CouponTemplateType type);

    void batchSendCoupon(CouponBatchVO couponBatchVO);

}