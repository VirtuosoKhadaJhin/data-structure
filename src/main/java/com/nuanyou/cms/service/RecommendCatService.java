package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.RecommendCat;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface RecommendCatService {

    RecommendCat saveNotNull(RecommendCat entity);

    void delete(Long id);

    List<RecommendCat> getIdNameList();

}