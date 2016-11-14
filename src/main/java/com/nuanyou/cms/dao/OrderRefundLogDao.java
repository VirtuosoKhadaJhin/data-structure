package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.OrderRefundLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Alan.ye on 2016/9/9.
 */
public interface OrderRefundLogDao extends JpaRepository<OrderRefundLog, Long>, JpaSpecificationExecutor {


    OrderRefundLog findByOrderId(Long id);
}
