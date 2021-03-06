package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/5.
 */
public interface DistrictDao extends JpaRepository<District, Long>, JpaSpecificationExecutor {

    @Query(value = "select new District(t.id,t.name) from District t ")
    List<District> getIdNameList();

    @Query(value = "select new District(t.id,t.name) from District t where  t.country.id in ?1")
    List<District> getIdNameList(List<Long> countryIds);

    @Query(value = "select new District(t.id,t.name) from District t where  t.country.id=?1")
    List<District> getIdNameList( Long countryId);

    @Query(value = "select new District(t.id,t.name) from District t where city.id=:id")
    List<District> findIdNameList(@Param("id") Long cityId);

    List<District> findByCountryId(Long id);

    @Transactional
    @Modifying
    @Query(value = "update District t set t.radio=:radio where t.id=:id")
    void updateDistrictRate(@Param("id") Long id, @Param("radio") BigDecimal radio);

    @Query(value = "select new District(t.id,t.name) from District t where t.city.id=:id")
    List<District> findIdNameListByCityId(@Param("id") Long id);
}
