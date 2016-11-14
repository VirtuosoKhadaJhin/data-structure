package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/8.
 */
public interface OrderService {
    Page<Order> findByCondition(Integer index, Order entity, TimeCondition time);

    Integer getBuyNum(Order order);

    void refund(Order entity);

    Order saveNotNull(Order entity);

    Page<Order> findRefundByCondition(int index, Order entity, TimeCondition time);


    void validate(Long id, Integer type,String cmsusername);
}


