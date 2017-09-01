package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.MerchantVisit;
import com.nuanyou.cms.model.VisitQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by young on 2017/9/1.
 */
public interface MerchantVisitService {

    Page<MerchantVisit> queryMerchantVisit (VisitQueryRequest request,Pageable pageable);
}
