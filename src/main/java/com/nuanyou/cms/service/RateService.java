package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Rate;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/7.
 */
public interface RateService {
    Page<Rate> findByCondition(Integer index, Rate entity);

    Rate saveNotNull(Rate entity);
}
