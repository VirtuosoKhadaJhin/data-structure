package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MerchantAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by libin on 2017/12/13.
 */
public interface MerchantAccountDao extends JpaRepository<MerchantAccount, Integer>, JpaSpecificationExecutor {

    @Query(value = "select count(0) from sl_merchant_account t where t.mchid = ?1" ,nativeQuery = true)
    Integer findByMchid(Long mchid);
}
