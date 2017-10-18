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

    List<ViewOrderExport> findExportByCondition(Order entity, TimeCondition time,List<Long> countryids, Pageable o);

    long countViewOrderExports(final Order entity, final TimeCondition time,List<Long> countryids);

    Page<Order> findRefundByCondition(Integer index, Order entity, TimeCondition time,List<Long> countryids);

    List<Order> findRefundByCondition(Order entity, TimeCondition time,final List<Long> countryids);

    Integer getBuyNum(Long userId);

    void refund(Order entity);

    Order saveNotNull(Order entity);

    void validate(Long id, Integer type, String cmsusername);

    Boolean checkUnRedeem(Long itemId);
}


