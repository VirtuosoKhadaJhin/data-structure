package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.RecommendCatDao;
import com.nuanyou.cms.entity.RecommendCat;
import com.nuanyou.cms.service.RecommendCatService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class RecommendCatServiceImpl implements RecommendCatService {

    @Autowired
    private RecommendCatDao dao;

    @Override
    public RecommendCat saveNotNull(RecommendCat entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        RecommendCat oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

    @Override
    public List<RecommendCat> getIdNameList() {
        return dao.getIdNameList();
    }
}