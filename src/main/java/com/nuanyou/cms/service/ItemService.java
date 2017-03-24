package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.ItemTuan;
import com.nuanyou.cms.model.ItemVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/20.
 */
public interface ItemService {

    Item saveNotNull(ItemVO vo);

    Item saveNotNull(ItemVO vo, List<ItemTuan> itemTuans);

    BigDecimal calcItemTuanPrice(Long id);

    List<Item> getIdNameList();

    List<Item> findItemTuans();

}
