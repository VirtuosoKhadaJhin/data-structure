package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.ItemDetailimg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2017/2/20.
 */
public interface ItemDetailimgDao extends JpaRepository<ItemDetailimg, Long>, JpaSpecificationExecutor {

}
