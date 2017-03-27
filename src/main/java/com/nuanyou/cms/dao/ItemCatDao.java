package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.ItemCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface ItemCatDao extends JpaRepository<ItemCat, Long>, JpaSpecificationExecutor {

    List<ItemCat> findByMerchantId(Long mchId);

    void deleteByMerchantId(Long id);

    @Query(value = "select new ItemCat(t.id, t.name, t.kpname) from ItemCat t where t.merchant.id=:id")
    List<ItemCat> findIdNameList(@Param("id") Long id);

    @Query(value = "select new ItemCat(t.id, t.name, t.kpname) from ItemCat t where t.merchant.id=:id and t.display=:display")
    List<ItemCat> findIdNameList(@Param("id") Long id, @Param("display") boolean display);

}
