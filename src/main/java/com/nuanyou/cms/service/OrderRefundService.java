package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.order.OrderRefundLog;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/10.
 */
public interface OrderRefundService {
    Page<OrderRefundLog> findByCondition(int index, OrderRefundLog entity);

    void validate(Long id, Integer type);

    OrderRefundLog saveNotNull(OrderRefundLog entity);
}
