package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Country;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface CountryService {

    List<Country> getIdNameList();

    Country saveNotNull(Country entity);


    Country findOne(Long countryId);
}
