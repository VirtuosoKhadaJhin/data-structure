package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.CommentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface CommentOrderDao extends JpaRepository<CommentOrder, Long>, JpaSpecificationExecutor {

}
