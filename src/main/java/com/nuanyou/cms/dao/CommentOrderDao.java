package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.CommentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface CommentOrderDao extends JpaRepository<CommentOrder, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query(value = "update CommentOrder t set t.display=?2 where t.id=?1")
    void showOrHideComment(Long id, Boolean isShow);
}
