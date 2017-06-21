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
public interface EntityNyLangsDictionaryDao extends JpaRepository<EntityNyLangsDictionary, Long>,JpaSpecificationExecutor {

    @Query(value = "select * from ny_langs_dictionary t where t.category.id=:BASE_NAME", nativeQuery = true)
    List<EntityNyLangsDictionary> findByCategoryId(@Param("BASE_NAME") Long id);

    @Transactional
    @Modifying
    @Query(value = "update EntityNyLangsDictionary ld set delFlag=true where keyCode=:keyCode")
    void logicalDelLangsDictionary(@Param("keyCode") String keyCode);

    @Transactional
    @Modifying
    @Query(value = "delete from EntityNyLangsDictionary lm where lm.keyCode=:keyCode")
    void delLangsDictionary(@Param("keyCode") String keyCode);

    @Query(value = "select * from ny_langs_dictionary t where t.KEY_CODE=:KEY_CODE", nativeQuery = true)
    List<EntityNyLangsDictionary> findByKeyCode(@Param("KEY_CODE") String keyCode);

}
