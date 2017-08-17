package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Discount;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.DiscountQueryParam;
import com.nuanyou.cms.model.MerchantQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Alan.ye on 2016/9/20.
 */
public interface DiscountService {

    Discount saveNotNull(Discount entity);

    void delete(Long id);

    Page<Discount> findDiscount (DiscountQueryParam param, Pageable pageable);

}