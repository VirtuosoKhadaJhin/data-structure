package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MerchantVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/9/1.
 */
public interface MerchantVisitDao extends JpaRepository<MerchantVisit, Long> ,JpaSpecificationExecutor{

    @Query(value = "select max (e.id),count (e.merchant.id),(select t from MerchantVisit t where t.id = e.merchant.id) from MerchantVisit e where e.merchant.id in ?1 group by e.merchant.id")
    Object[][] findLatestVisit(List<Long> merchantIds);

    @Query(value = "select e from MerchantVisit e where e.id in ?1 order by e.updateTime desc ")
    Page<MerchantVisit> findLatestVisitDetail(List<Long> visitIds, Pageable pageable);

    @Query(value = "select e from MerchantVisit e where e.merchant.id = ?1 and e.createTime <?2 order by e.createTime desc")
    List<MerchantVisit> findPreviousVisit(Long mchId,Date createTime);
}
