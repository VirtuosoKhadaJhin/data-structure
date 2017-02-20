package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ItemDetailimgDao;
import com.nuanyou.cms.entity.ItemDetailimg;
import com.nuanyou.cms.service.ItemDetailimgService;
import com.nuanyou.cms.service.ItemDetailimgService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class ItemDetailimgServiceImpl implements ItemDetailimgService {

    @Autowired
    private ItemDetailimgDao merchantHeadimgDao;

    @Override
    public ItemDetailimg saveNotNull(ItemDetailimg entity) {
        if (entity.getId() == null) {
            Integer sort = entity.getSort();
            if (sort == null)
                entity.setSort(999);
            entity = merchantHeadimgDao.save(entity);
            if (sort != null && sort == 0)
                setTop(entity.getId());
            return entity;
        }
        ItemDetailimg oldEntity = merchantHeadimgDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantHeadimgDao.save(oldEntity);
    }

    @Override
    public List<ItemDetailimg> find(ItemDetailimg entity) {
        return merchantHeadimgDao.findAll(Example.of(entity), new Sort("sort"));
    }

    @Override
    public void setTop(Long id) {
        ItemDetailimg headimg = merchantHeadimgDao.findOne(id);
        if (headimg == null)
            return;
        Long referId = headimg.getReferId();
        if (referId == null)
            return;
        List<ItemDetailimg> merchantHeadimgs = find(new ItemDetailimg(referId));
        int sort = 1;
        for (ItemDetailimg merchantHeadimg : merchantHeadimgs) {
            if (merchantHeadimg.getId().equals(id)) {
                merchantHeadimg.setSort(0);
            } else {
                merchantHeadimg.setSort(sort++);
            }
        }
        merchantHeadimgDao.save(merchantHeadimgs);
    }

}




