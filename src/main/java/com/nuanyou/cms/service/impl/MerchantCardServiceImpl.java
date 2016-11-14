package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.MerchantCardDao;
import com.nuanyou.cms.entity.MerchantCard;
import com.nuanyou.cms.service.MerchantCardService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class MerchantCardServiceImpl implements MerchantCardService {

    @Autowired
    private MerchantCardDao merchantCardDao;

    @Override
    public MerchantCard saveNotNull(MerchantCard entity) {
        if (entity.getId() == null) {
            return merchantCardDao.save(entity);
        }
        MerchantCard oldEntity = merchantCardDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantCardDao.save(oldEntity);
    }

}