package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private UserService userService;

    @Override
    public List<Country> getIdNameList() {
        List<Long> countryIds = userService.findUserCountryId();
        if (countryIds != null && countryIds.size() > 0)
            return countryDao.getIdNameList(countryIds);
        else
            return countryDao.findAllCountries();

    }

    @Override
    public Country saveNotNull(Country entity) {
        if (entity.getId() == null) {
            return countryDao.save(entity);
        }
        Country oldEntity = countryDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return countryDao.save(oldEntity);
    }

    @Override
    public Country findOne(Long countryId) {
        return this.countryDao.findOne(countryId);
    }

    @Override
    public List<Country> findAllCountries() {
        return countryDao.findAllCountries();
    }

    @Override
    public List<String> countryCodes() {
        List<Long> countryIds = userService.findUserCountryId();
        if (CollectionUtils.isEmpty(countryIds)) {
            return Lists.newArrayList();
        }
        List<Country> countries = countryDao.getIdNameCodeList(countryIds);
        Collection codes = CollectionUtils.collect(countries, new Transformer() {
            @Override
            public Object transform(Object input) {
                return ((Country) input).getCode();
            }
        });
        return Lists.newArrayList(codes);
    }
}
