package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.push.PushDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by yangkai on 2017/2/15.
 */
public interface PushDetailDao extends JpaRepository<PushDetail, Long>, JpaSpecificationExecutor {

    Long countByGroupIdAndStatusAndDeleted(Long groupId, Boolean status, Boolean deleted);

}
