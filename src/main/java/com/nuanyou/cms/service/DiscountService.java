package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Discount;

/**
 * Created by Alan.ye on 2016/9/20.
 */
public interface DiscountService {

    Discount saveNotNull(Discount entity);

    void delete(Long id);

}