package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MerchantStats;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface MerchantStatsDao extends JpaRepository<MerchantStats, Long> {

    MerchantStats getByMchId(Long id);

}