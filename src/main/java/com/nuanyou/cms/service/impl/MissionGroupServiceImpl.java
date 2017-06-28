package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.BdCountryDao;
import com.nuanyou.cms.dao.CityDao;
import com.nuanyou.cms.dao.NyMissionGroupBdDao;
import com.nuanyou.cms.dao.NyMissionGroupDao;
import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.MissionGroupManagerVo;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.MissionGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by sharp on 2017/6/28 - 15:56
 */
@Service
public class MissionGroupServiceImpl implements MissionGroupService {
    
    @Autowired
    private NyMissionGroupDao groupDao;
    
    @Autowired
    private NyMissionGroupBdDao groupBdDao;
    
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private BdCountryDao bdCountryDao;
    
    @Autowired
    private CityDao cityDao;
    
    @Override
    public Page<MissionGroupManagerVo> findAllGroups(MissionGroupManagerVo requestVo) {
        //分页请求
        final Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());
        
        List<MissionGroup> groups = groupDao.findAll();
        
        List<MissionGroupManagerVo> allCate = convertToBdUserManagerVo(groups);
        
        Page<MissionGroupManagerVo> pageVOs = new PageImpl<>(allCate, pageable, groups.size());
        
        return pageVOs;
    }
    
    @Override
    public List<BdCountry> findAllCountries() {
        List<BdCountry> countries = bdCountryDao.findAll();
        return countries;
    }
    
    @Override
    public List<City> findAllCities() {
        List<City> cities = cityDao.findAll();
        return cities;
    }
    
    @Override
    public void saveGroup(MissionGroup group) {
        groupDao.save(group);
    }
    
    @Override
    public List<MissionGroup> findByCountryAndCityId(final Long country, final Long city) {
        if (country == null && city == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (country != null) {
                    predicate.add(cb.equal(root.get("country").get("id"), country));
                }
                if (city != null) {
                    predicate.add(cb.equal(root.get("id"), city));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return cityDao.findAll(specification);
    }
    
    private List<MissionGroupManagerVo> convertToBdUserManagerVo(List<MissionGroup> groups) {
        ArrayList<MissionGroupManagerVo> list = new ArrayList<>();
        for (MissionGroup group : groups) {
            MissionGroupManagerVo vo = new MissionGroupManagerVo();
            
            Country country = countryService.findOne(group.getCountryId());
            City city = cityDao.findOne(group.getCityId());
            
            vo.setGroup(group);
            vo.setCountry(country);
            vo.setCity(city);
            
            list.add(vo);
        }
        
        return list;
    }
}
