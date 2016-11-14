package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.RecommendDao;
import com.nuanyou.cms.entity.Recommend;
import com.nuanyou.cms.service.RecommendService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendDao dao;

    @Override
    public Recommend saveNotNull(Recommend entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        Recommend oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

}