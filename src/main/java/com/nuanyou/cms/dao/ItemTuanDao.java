package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.ItemTuan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface ItemTuanDao extends JpaRepository<ItemTuan, Long> {

    List<ItemTuan> findByItemId(Long itemId);

    void deleteByItemId(Long itemId);

}