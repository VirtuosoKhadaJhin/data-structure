package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.OrderVirtualMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by mylon.sun on 2018/1/10.
 */
public interface OrderVirtualMailDao extends JpaRepository<OrderVirtualMail, Long>, JpaSpecificationExecutor {

    OrderVirtualMail findByOrderId(Long orderId);

}
