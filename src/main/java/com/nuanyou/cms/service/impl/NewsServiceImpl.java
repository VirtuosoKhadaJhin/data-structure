package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.NewsDao;
import com.nuanyou.cms.entity.News;
import com.nuanyou.cms.service.NewsService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/8.
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao dao;

    @Override
    public News saveNotNull(News entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        News oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

}