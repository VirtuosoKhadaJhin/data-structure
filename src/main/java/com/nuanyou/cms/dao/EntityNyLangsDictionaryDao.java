package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Byron on 2017/5/26.
 */
public interface EntityNyLangsDictionaryDao extends JpaRepository<EntityNyLangsDictionary, Long>,JpaSpecificationExecutor {

    List<EntityNyLangsDictionary> findByCategoryId(Long id);
}
