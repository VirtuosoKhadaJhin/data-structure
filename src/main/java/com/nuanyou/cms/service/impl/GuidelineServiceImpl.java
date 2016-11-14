
package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.GuidelineDao;
import com.nuanyou.cms.entity.Guideline;
import com.nuanyou.cms.service.GuidelineService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class GuidelineServiceImpl implements GuidelineService {

    @Autowired
    private GuidelineDao dao;

    @Override
    public Guideline saveNotNull(Guideline entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        Guideline oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

}