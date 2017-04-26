package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Felix on 2016/9/29.
 */
public interface FeatureDao extends JpaRepository<Feature, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query(value = "update Feature t set sort=:sort where id=:id")
    void updateSort(@Param("id") Long id, @Param("sort") Integer radio);

    @Query(value = "select q.sort from Feature q where q.id = ?1")
    Integer getSortById(Long id);

}