package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CouponTemplateDao;
import com.nuanyou.cms.entity.coupon.CouponGroup;
import com.nuanyou.cms.entity.coupon.CouponTemplate;
import com.nuanyou.cms.service.CouponTemplateService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/10/13.
 */
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    @Override
    public CouponTemplate saveNotNull(CouponTemplate entity) {
        if (entity.getId() == null) {
            return couponTemplateDao.save(entity);
        }
        CouponTemplate oldEntity = couponTemplateDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return couponTemplateDao.save(oldEntity);
    }
}
