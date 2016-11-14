package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/8.
 */
public interface OrderItemDao extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor {

}
