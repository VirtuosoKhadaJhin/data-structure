package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.ItemDirectmail;
import com.nuanyou.cms.entity.ItemTuan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Alan.ye on 2016/10/18.
 */
public interface ItemDirectmailDao extends JpaRepository<ItemDirectmail, Long> {

    void deleteByItemId(Long itemId);

}