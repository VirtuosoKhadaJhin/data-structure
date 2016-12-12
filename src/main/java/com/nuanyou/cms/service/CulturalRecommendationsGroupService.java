package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CulturalRecommendationsGroup;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface CulturalRecommendationsGroupService {

    CulturalRecommendationsGroup saveNotNull(CulturalRecommendationsGroup entity);

    void delete(Long id);

    List<CulturalRecommendationsGroup> getIdNameList();

}