package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.RecommendCatDao;
import com.nuanyou.cms.entity.Recommend;
import com.nuanyou.cms.entity.RecommendCat;
import com.nuanyou.cms.service.RecommendCatService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/8.
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

    public void delete(Long id) {
        RecommendCat entity = dao.findOne(id);
        if (entity != null) {
            entity.setDisplay(false);
            dao.save(entity);
        }
    }

    @Override
    public List<RecommendCat> getIdNameList() {
        return dao.getIdNameList();
    }
}