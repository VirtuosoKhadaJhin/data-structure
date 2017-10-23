package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.ItemTuanImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mylon.sun on 2017/10/23.
 */
public interface ItemTuanImgDao extends JpaRepository<ItemTuanImg, Long> {

    List<ItemTuanImg> findByItemId(Long itemId);

}
