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

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where t.district.country.id in ?1")
    List<Merchant> getIdNameList(List<Long> countryIds);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where t.display=?1 and t.district.country.id in ?2")
    List<Merchant> getIdNameList(boolean display,List<Long> countryIds);

    @Query(value = "select t.kpname from Merchant t where t.id=?1")
    String getLocalName(Long id);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where t.display=?1")
    List<Merchant> getIdNameList(boolean display);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where mcat.id=:id")
    List<Merchant> findIdNameList(@Param("id") Long catid);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where t.district.country.id=:id")
    List<Merchant> findIdNameByCountry(@Param("id") Long country);

    @Query(value = "select new Merchant(t.id,t.name) from Merchant t where t.district.id=:id")
    List<Merchant> findIdNameByDistrict(@Param("id")Long district);

    @Override
    Merchant save(Merchant entity);
}
