package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MerchantVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by young on 2017/9/1.
 */
public interface MerchantVisitDao extends JpaRepository<MerchantVisit, Long> ,JpaSpecificationExecutor{
}
