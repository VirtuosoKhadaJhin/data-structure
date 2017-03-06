package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.push.PushDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by yangkai on 2017/2/15.
 */
public interface PushDetailDao extends JpaRepository<PushDetail, Long>, JpaSpecificationExecutor {

    @Query("select count(e) from PushDetail e where e.groupId=?1 and e.status=?2 and e.deleted=?3 " +
            "and (e.startTime is null or e.startTime<=?4) and (e.endTime is null or e.endTime>=?4)")
    Long countForGroup(Long groupId, Boolean status, Boolean deleted, Date now);

}
