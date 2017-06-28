package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.City;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface CityDao extends JpaRepository<City, Long>, JpaSpecificationExecutor {


    @Query(value = "select new City(t.id,t.name) from City t where t.country.id=:id")
    List<City> findIdNameList(@Param("id") Long id);
}
