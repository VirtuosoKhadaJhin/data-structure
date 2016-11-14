package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.MerchantCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface MerchantCatDao extends JpaRepository<MerchantCat, Long>, JpaSpecificationExecutor {

    List<MerchantCat> findByPcat(MerchantCat pid);

    @Query(value = "select new MerchantCat(t.id,t.name) from  MerchantCat t")
    List<MerchantCat> getIdNameList();
}
