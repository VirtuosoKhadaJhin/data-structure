package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.MerchantHeadimgDao;
import com.nuanyou.cms.entity.MerchantHeadimg;
import com.nuanyou.cms.service.MerchantHeadimgService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class MerchantHeadimgServiceImpl implements MerchantHeadimgService {

    @Autowired
    private MerchantHeadimgDao merchantHeadimgDao;

    @Override
    public MerchantHeadimg saveNotNull(MerchantHeadimg entity) {
        if (entity.getId() == null) {
            Integer sort = entity.getSort();
            if (sort == null)
                entity.setSort(999);
            entity = merchantHeadimgDao.save(entity);
            if (sort != null && sort == 0)
                setTop(entity.getId());
            return entity;
        }
        MerchantHeadimg oldEntity = merchantHeadimgDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantHeadimgDao.save(oldEntity);
    }

    @Override
    public List<MerchantHeadimg> find(MerchantHeadimg entity) {
        return merchantHeadimgDao.findAll(Example.of(entity), new Sort("sort"));
    }

    @Override
    public void setTop(Long id) {
        MerchantHeadimg headimg = merchantHeadimgDao.findOne(id);
        if (headimg == null)
            return;
        List<MerchantHeadimg> merchantHeadimgs = find(new MerchantHeadimg(headimg.getMchId()));
        int sort = 1;
        for (MerchantHeadimg merchantHeadimg : merchantHeadimgs) {
            if (merchantHeadimg.getId().equals(id)) {
                merchantHeadimg.setSort(0);
            } else {
                merchantHeadimg.setSort(sort++);
            }
        }
        merchantHeadimgDao.save(merchantHeadimgs);
    }

}




