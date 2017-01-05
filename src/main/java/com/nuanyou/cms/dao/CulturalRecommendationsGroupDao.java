package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.CulturalRecommendationsGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface CulturalRecommendationsGroupDao extends JpaRepository<CulturalRecommendationsGroup, Long> {

    @Query(value = "select new CulturalRecommendationsGroup(t.id,t.title) from CulturalRecommendationsGroup t")
    List<CulturalRecommendationsGroup> getIdNameList();

}
