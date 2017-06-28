package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.mission.MissionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Byron on 2017/6/27.
 */
public interface MissionTaskDao extends JpaRepository<MissionTask, Long>, JpaSpecificationExecutor {

    @Query(value = "update MissionTask t set status=:status, remark=:remark where mchId=:mchId")
    void updateTaskStatus(@Param("mchId") Long id, @Param("status") int status, @Param("remark") String remark);
}
