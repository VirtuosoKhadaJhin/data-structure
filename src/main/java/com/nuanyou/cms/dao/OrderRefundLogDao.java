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

    @Query("select t from OrderRefundLog t where t.order.id =?1 and t.id=(select min(k.id) from OrderRefundLog k where k.order.id =?1) ")
    OrderRefundLog findByOrderId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE OrderRefundLog set status=?2 where id=?1")
    void updateOrderRefundLogStatus(Long id, Integer status);

}
