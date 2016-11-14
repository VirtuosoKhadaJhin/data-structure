package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.order.OrderVouchCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface OrderVouchCardDao extends JpaRepository<OrderVouchCard, Long>, JpaSpecificationExecutor {

    List<OrderVouchCard> findByOrderId(Long id);
    
}