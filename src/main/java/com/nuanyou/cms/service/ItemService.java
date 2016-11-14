package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.ItemTuan;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/20.
 */
public interface ItemService {

    Item saveNotNull(Item entity);

    Item saveNotNull(Item item, List<ItemTuan> itemTuans);

    BigDecimal calcItemTuanPrice(Long id);

    List<Item> getIdNameList();

    List<Item> findItemTuans();

}
