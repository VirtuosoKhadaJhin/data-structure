package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CulturalRecommendationsGroupDao;
import com.nuanyou.cms.entity.CulturalRecommendationsGroup;
import com.nuanyou.cms.service.CulturalRecommendationsGroupService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/8.
 */
@Service
public class CulturalRecommendationsGroupServiceImpl implements CulturalRecommendationsGroupService {

    @Autowired
    private CulturalRecommendationsGroupDao dao;

    @Override
    public CulturalRecommendationsGroup saveNotNull(CulturalRecommendationsGroup entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        CulturalRecommendationsGroup oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

    public void delete(Long id) {
        CulturalRecommendationsGroup entity = dao.findOne(id);
        if (entity != null) {
            entity.setDisplay(false);
            entity.setDeleted(true);
            dao.save(entity);
        }
    }

    @Override
    public List<CulturalRecommendationsGroup> getIdNameList() {
        return dao.getIdNameList();
    }
}