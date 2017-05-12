package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.entity.order.ViewOrderExport;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Felix on 2016/9/8.
 */
public interface OrderService {

    Page<Order> findByCondition(Integer index, Order entity, TimeCondition time, Pageable pageable);

    List<ViewOrderExport> findExportByCondition(Integer index, Order entity, TimeCondition time, Pageable o);

    Page<Order> findRefundByCondition(Integer index, Order entity, TimeCondition time);

    List<Order> findRefundByCondition(Order entity, TimeCondition time);

    Integer getBuyNum(Long userId);

    void refund(Order entity);

    Order saveNotNull(Order entity);

    void validate(Long id, Integer type, String cmsusername);


}


