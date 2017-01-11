package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface ItemDao extends JpaRepository<Item, Long> {

    List<Item> findByMerchantIdAndCatId(Long mchId, Long catId);

    void deleteByMerchantId(Long id);

    @Query(value = "select new Item(t.id,t.name) from Item t")
    List<Item> getIdNameList();

    @Query(value = "select new Item(t.id,t.name) from Item t where t.type=2")
    List<Item> findItemTuans();
}
