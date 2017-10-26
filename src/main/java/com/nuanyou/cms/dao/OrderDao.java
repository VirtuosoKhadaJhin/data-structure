package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
public interface OrderDao extends JpaRepository<Order, Long>, JpaSpecificationExecutor {

    @Query(value = "SELECT COUNT(1) from ny_order WHERE ny_order.status not in(0,4) and userid=:id", nativeQuery = true)
    Integer getBuyNum(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(id) from ny_order WHERE ny_order.orderstatus=1 and ny_order.status=1 and ny_order.id in?1", nativeQuery = true)
    Integer findUnRedeemedAndIdIn(List<Long> orderIds);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ny_order SET status=?2 where id=?1", nativeQuery = true)
    void updateOrderRefundLogStatus(Long id, Integer status);

}
