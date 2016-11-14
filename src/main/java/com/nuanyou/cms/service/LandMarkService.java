package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Landmark;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/14.
 */
public interface LandMarkService {
    Page<Landmark> findByCondition(int index, Landmark entity);

    Landmark saveNotNull(Landmark entity);
}
