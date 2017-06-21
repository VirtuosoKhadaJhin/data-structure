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
    @Query(value = "update EntityNyLangsMessageTip lm set lm.delFlag=true where lm.keyCode=:keyCode")
    void logicalDelLangsMessageTip(@Param("keyCode") String keyCode);

    @Query(value = "select lm from EntityNyLangsMessageTip lm where lm.keyCode=:keyCode")
    EntityNyLangsMessageTip findByKeyCode(@Param("keyCode") String keyCode);


}
