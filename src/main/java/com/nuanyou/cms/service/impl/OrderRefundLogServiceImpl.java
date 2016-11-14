package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.OrderRefundLogDao;
import com.nuanyou.cms.entity.order.OrderRefundLog;
import com.nuanyou.cms.service.OrderRefundLogService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/10/15.
 */
@Service
public class OrderRefundLogServiceImpl implements OrderRefundLogService {
    @Autowired private OrderRefundLogDao orderRefundLogDao;

    @Override
    public OrderRefundLog saveNotNull(OrderRefundLog entity) {
        if (entity.getId() == null) {
            return orderRefundLogDao.save(entity);
        }
        OrderRefundLog oldEntity = orderRefundLogDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return orderRefundLogDao.save(oldEntity);
    }
}
