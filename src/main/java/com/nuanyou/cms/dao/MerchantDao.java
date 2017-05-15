package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface MerchantDao extends JpaRepository<Merchant, Long>, JpaSpecificationExecutor {

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t")
    List<Merchant> getIdNameList();

    @Query(value = "select t.kpname from Merchant t where t.id=?1")
    String getLocalName(Long id);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where t.display=?1")
    List<Merchant> getIdNameList(boolean display);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where mcat.id=:id")
    List<Merchant> findIdNameList(@Param("id") Long catid);

    @Override
    Merchant save(Merchant entity);
}
