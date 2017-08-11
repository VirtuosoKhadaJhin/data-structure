package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Rate;
import com.nuanyou.cms.entity.RecommendCat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface RecommendCatService {

    RecommendCat saveNotNull(RecommendCat entity);

    void delete(Long id);

    List<RecommendCat> getIdNameList();

    Page<RecommendCat> findByCondition(RecommendCat entity, Pageable pageable);

}