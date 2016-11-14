package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CouponGroupDao;
import com.nuanyou.cms.entity.coupon.CouponGroup;
import com.nuanyou.cms.service.CouponGroupService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/10/13.
 */
@Service
public class CouponGroupServiceImpl implements CouponGroupService {
    @Autowired
    private CouponGroupDao couponGroupDao;

    @Override
    public CouponGroup saveNotNull(CouponGroup entity) {
        if (entity.getId() == null) {
            return couponGroupDao.save(entity);
        }
        CouponGroup oldEntity = couponGroupDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return couponGroupDao.save(oldEntity);
    }
}
