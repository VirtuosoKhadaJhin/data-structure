package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface CityService {

    Page<City> findByCondition(City entity, Pageable pageable);

    City saveNotNull(City entity);

    List<City> findCityByCountryId(Long countryId);
}
