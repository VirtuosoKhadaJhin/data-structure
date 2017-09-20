package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.MerchantVisit;
import com.nuanyou.cms.entity.VisitType;
import com.nuanyou.cms.model.VisitQueryRequest;
import com.nuanyou.cms.model.visit.VisitChangeVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by young on 2017/9/1.
 */
public interface MerchantVisitService {

    Page<MerchantVisit> queryMerchantVisit (VisitQueryRequest request,Pageable pageable);

    List<VisitType> findVisitTypes();

    Page<MerchantVisit> queryLatestVisit (VisitQueryRequest request,Pageable pageable);

    VisitChangeVo getChange(Long visitId);
}
