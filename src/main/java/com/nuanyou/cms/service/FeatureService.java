package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Feature;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/7.
 */
public interface FeatureService {
    Page<Feature> findByCondition(Integer index, Feature Feature);

    Feature saveNotNull(Feature entity);

    void delete(Long id);

}