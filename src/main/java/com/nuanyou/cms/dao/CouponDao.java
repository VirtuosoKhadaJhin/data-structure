package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alan.ye on 2016/9/27.
 */
public interface CouponDao extends JpaRepository<Coupon, Long> {

}