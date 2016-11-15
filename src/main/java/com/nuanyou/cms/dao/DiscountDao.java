package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alan.ye on 2016/9/9.
 */
public interface DiscountDao extends JpaRepository<Discount, Long> {
}
