package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return countryDao.getIdNameList(countryIds);
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
}
