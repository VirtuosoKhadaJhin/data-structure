package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.coupon.CouponTemplate;
import com.nuanyou.cms.entity.enums.CouponTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Felix on 2016/9/21.
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Long> {

    @Query(value = "select new CouponTemplate(t.id,t.title) from CouponTemplate t where t.type=?1")
    List<CouponTemplate> findIdNameList(CouponTemplateType type);

}
