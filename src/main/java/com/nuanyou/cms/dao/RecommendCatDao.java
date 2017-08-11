package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.RecommendCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/29.
 */
public interface RecommendCatDao extends JpaRepository<RecommendCat, Long> ,JpaSpecificationExecutor{

    @Query(value = "select new RecommendCat(t.id,t.title) from RecommendCat t")
    List<RecommendCat> getIdNameList();

}
