package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.MerchantCardStatsDao;
import com.nuanyou.cms.entity.MerchantCardStats;
import com.nuanyou.cms.service.MerchantCardStatsService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class MerchantCardStatsServiceImpl implements MerchantCardStatsService {

    @Autowired
    private MerchantCardStatsDao merchantCardStatsDao;

    @Override
    public MerchantCardStats saveNotNull(MerchantCardStats entity) {
        if (entity.getId() == null) {
            return merchantCardStatsDao.save(entity);
        }
        MerchantCardStats oldEntity = merchantCardStatsDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantCardStatsDao.save(oldEntity);
    }

}