package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Felix on 2016/9/30.
 */
public interface RankDao extends JpaRepository<Rank, Long>, JpaSpecificationExecutor {

}
