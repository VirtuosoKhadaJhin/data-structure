package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Quick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface QuickDao extends JpaRepository<Quick, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query(value = "update Quick t set sort=:sort where id=:id")
    void updateSort(@Param("id") Long id, @Param("sort") Integer radio);

    @Query(value = "select q.sort from Quick q where q.id = ?1")
    Integer getSortById(Long id);
}