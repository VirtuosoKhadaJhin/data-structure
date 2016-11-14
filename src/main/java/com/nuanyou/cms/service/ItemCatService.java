package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.ItemCat;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/7.
 */
public interface ItemCatService {
    Page<ItemCat> findByCondition(Integer index, ItemCat entity);

    ItemCat saveNotNull(ItemCat entity);
}
