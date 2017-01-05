package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CulturalRecommendations;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface CulturalRecommendationsService {

    CulturalRecommendations saveNotNull(CulturalRecommendations entity);

    void delete(Long id);

}