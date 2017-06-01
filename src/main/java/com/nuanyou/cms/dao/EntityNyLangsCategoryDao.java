package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.EntityNyLangsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Byron on 2017/5/27.
 */
public interface EntityNyLangsCategoryDao extends JpaRepository<EntityNyLangsCategory, Long>, JpaSpecificationExecutor {

}
