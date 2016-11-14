package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.City;
import org.springframework.data.domain.Page;

/**
 * Created by Felix on 2016/9/7.
 */
public interface CityService {

    Page<City> findByCondition(Integer index, City entity);

    City saveNotNull(City entity);


}
