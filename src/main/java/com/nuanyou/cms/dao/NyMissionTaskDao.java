package com.nuanyou.cms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sharp on 2017/6/28 - 15:27
 */
public interface NyMissionTaskDao extends JpaRepository<MissionTask, Long>, JpaSpecificationExecutor {
}
