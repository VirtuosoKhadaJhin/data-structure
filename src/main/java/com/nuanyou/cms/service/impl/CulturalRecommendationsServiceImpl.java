package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CulturalRecommendationsDao;
import com.nuanyou.cms.entity.CulturalRecommendations;
import com.nuanyou.cms.service.CulturalRecommendationsService;
import com.nuanyou.cms.service.CulturalRecommendationsService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Alan.ye on 2016/9/8.
 */
@Service
public class CulturalRecommendationsServiceImpl implements CulturalRecommendationsService {

    @Autowired
    private CulturalRecommendationsDao dao;

    @Override
    public CulturalRecommendations saveNotNull(CulturalRecommendations entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        CulturalRecommendations oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

    public void delete(Long id) {
        CulturalRecommendations entity = dao.findOne(id);
        if (entity != null) {
            entity.setDisplay(false);
            dao.save(entity);
        }
    }

}