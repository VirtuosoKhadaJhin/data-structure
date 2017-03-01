package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.OrderDirectMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2017/2/28.
 */
public interface OrderDirectMailDao extends JpaRepository<OrderDirectMail, Long>, JpaSpecificationExecutor {
    OrderDirectMail findByOrderId(Long id);
}
