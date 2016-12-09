package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Landmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Felix on 2016/9/14.
 */
public interface LandMarkService {

    Page<Landmark> findByCondition(Landmark entity, Pageable pageable);

    Landmark saveNotNull(Landmark entity);

}