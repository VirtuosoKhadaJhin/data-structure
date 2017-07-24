package com.nuanyou.cms.service;


import com.nuanyou.cms.entity.EntityBdMerchantCollectionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by young on 2017/6/28.
 */
public interface MerchantCollectionCodeService {

    Long findMchIdByCode(String code);

    Boolean hasCollectionCode(String code);

    EntityBdMerchantCollectionCode findCollectionCode(String code);

    void saveEntityBdMerchantCollectionCode(EntityBdMerchantCollectionCode entityBdMerchantCollectionCode);

    List<EntityBdMerchantCollectionCode> findEntityBdMerchantCollectionCodesByMchId(Long mchId);

    boolean bindNumberLink(Long number, String target) throws Exception;

    boolean unbindNumberLink(Long number) throws Exception;

    Page<EntityBdMerchantCollectionCode> query (EntityBdMerchantCollectionCode entity, Pageable pageable);

}
