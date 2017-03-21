package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.MerchantCard;
import com.nuanyou.cms.util.TimeCondition;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/10/9.
 */
public interface MerchantCardService {

    MerchantCard saveNotNull(MerchantCard entity);

    Page<MerchantCard> findByCondition(Integer index, final MerchantCard entity, final TimeCondition validTime);

    void delete(Long id);

}