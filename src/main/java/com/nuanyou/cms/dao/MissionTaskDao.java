package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.mission.MissionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Byron on 2017/6/27.
 */
public interface MissionTaskDao extends JpaRepository<MissionTask, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query("update MissionTask t set status=?2, remark=?3, auditor=?4, auditDt=?5, updateDt=?5 where mchId=?1")
    void updateTaskStatus(Long mchId, int status, String remark, Long auditor, Date date);

    @Transactional
    @Modifying
    @Query("update MissionTask t set bdId=?1, status=?2, distrDt=?3,updateDt=?4 where id in ?5")
    void distributeTask(Long bdId, int status, Date distrDt, Date date, List<Long> taskIds);
}
