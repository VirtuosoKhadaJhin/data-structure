package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.CulturalRecommendationsGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface CulturalRecommendationsGroupService {

    CulturalRecommendationsGroup saveNotNull(CulturalRecommendationsGroup entity);

    void delete(Long id);

    List<CulturalRecommendationsGroup> getIdNameList();

    Page<CulturalRecommendationsGroup> findByCondition(final CulturalRecommendationsGroup entity, Pageable pageable);

}