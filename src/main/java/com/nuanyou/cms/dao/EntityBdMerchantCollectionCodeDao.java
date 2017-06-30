package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.EntityBdMerchantCollectionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by young on 2017/6/28.
 */
public interface EntityBdMerchantCollectionCodeDao extends JpaRepository<EntityBdMerchantCollectionCode, Long> {

    EntityBdMerchantCollectionCode findByCollectionCode(String collectionCode);

    List<EntityBdMerchantCollectionCode> findByMchId (Long mchId);

}
