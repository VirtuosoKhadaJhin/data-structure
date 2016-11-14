package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ItemDirectmailDao;
import com.nuanyou.cms.entity.ItemDirectmail;
import com.nuanyou.cms.service.ItemDirectmailService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Alan.ye on 2016/10/19.
 */
@Service
public class ItemDirectmailServiceImpl implements ItemDirectmailService {

    @Autowired
    private ItemDirectmailDao dao;

    @Override
    public ItemDirectmail saveNotNull(ItemDirectmail entity) {
        if (entity.getId() == null) {
            return dao.save(entity);
        }
        ItemDirectmail oldEntity = dao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return dao.save(oldEntity);
    }

}