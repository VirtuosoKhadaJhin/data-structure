package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Byron on 2017/6/12.
 */
public interface EntityNyLangsMessageTipDao extends JpaRepository<EntityNyLangsMessageTip, Long>, JpaSpecificationExecutor {

}
