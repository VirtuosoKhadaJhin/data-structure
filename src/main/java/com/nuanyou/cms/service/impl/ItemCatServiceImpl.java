package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ItemCatDao;
import com.nuanyou.cms.entity.ItemCat;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.ItemCatService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Override
    public Page<ItemCat> findByCondition(Integer index, ItemCat entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher e = ExampleMatcher.matching();
        ExampleMatcher.GenericPropertyMatcher g = ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.ENDING);
        if (entity.getId() != null) {
            e = e.withMatcher("id", g.exact());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            e = e.withMatcher("name", g.contains());
        } else {
            entity.setName(null);
        }
        if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
            e = e.withMatcher("merchant.id", g.exact());
        }
        if (entity.getDisplay() != null) {
            e = e.withMatcher("display", g.exact());
        } else {
            entity.setDisplay(null);
        }
        return itemCatDao.findAll(Example.of(entity, e), pageable);
    }

    @Override
    public ItemCat saveNotNull(ItemCat entity) {
        if (entity.getId() == null) {
            return itemCatDao.save(entity);
        }
        ItemCat oldEntity = itemCatDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return itemCatDao.save(oldEntity);
    }
}
