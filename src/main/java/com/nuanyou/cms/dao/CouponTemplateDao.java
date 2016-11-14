package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.coupon.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/21.
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Long>, JpaSpecificationExecutor {
}
