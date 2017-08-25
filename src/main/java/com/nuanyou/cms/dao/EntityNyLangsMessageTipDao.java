package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Byron on 2017/6/12.
 */
public interface EntityNyLangsMessageTipDao extends JpaRepository<EntityNyLangsMessageTip, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query(value = "update EntityNyLangsMessageTip lm set lm.delFlag=:delFlag where lm.keyCode=:keyCode")
    void setDelFLagKeyCodeFor(@Param("delFlag") Boolean delFlag, @Param("keyCode") String keyCode);

    @Query(value = "select lm from EntityNyLangsMessageTip lm where lm.keyCode=:keyCode and lm.delFlag=false")
    EntityNyLangsMessageTip findByKeyCode(@Param("keyCode") String keyCode);

    @Query(value = "select lm from EntityNyLangsMessageTip lm where lm.keyCode in:keyCodes and lm.delFlag=false")
    List<EntityNyLangsMessageTip> findByKeyCodes(@Param("keyCodes") List<String> keyCodes);

}
