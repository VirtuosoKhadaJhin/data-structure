package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Byron on 2017/5/26.
 */
public interface EntityNyLangsDictionaryDao extends JpaRepository<EntityNyLangsDictionary, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query("update EntityNyLangsDictionary d set d.delFlag=:delFlag where d.keyCode=:keyCode")
    void setDelFLagKeyCodeFor(@Param("delFlag") Boolean delFlag, @Param("keyCode") String keyCode);

    List<EntityNyLangsDictionary> findByCategoryId(Long id);

    List<EntityNyLangsDictionary> findByKeyCode(String keyCode);

}
