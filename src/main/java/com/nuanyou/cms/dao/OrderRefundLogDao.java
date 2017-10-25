package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.OrderRefundLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Alan.ye on 2016/9/9.
 */
public interface OrderRefundLogDao extends JpaRepository<OrderRefundLog, Long>, JpaSpecificationExecutor {

    OrderRefundLog findByOrderId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE OrderRefundLog set status=?2 where id=?1")
    void updateOrderRefundLogStatus(Long id, Integer status);

}
