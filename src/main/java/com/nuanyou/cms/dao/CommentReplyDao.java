package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface CommentReplyDao extends JpaRepository<CommentReply, Long>, JpaSpecificationExecutor {

}
