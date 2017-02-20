package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.ItemDetailimg;

import java.util.List;

/**
 * Created by Alan.ye on 2017/2/20.
 */
public interface ItemDetailimgService {

    ItemDetailimg saveNotNull(ItemDetailimg entity);

    List<ItemDetailimg> find(ItemDetailimg entity);

    void setTop(Long id);

}