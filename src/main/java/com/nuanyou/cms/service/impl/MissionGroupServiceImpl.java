package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.BdCountryDao;
import com.nuanyou.cms.dao.BdUserDao;
import com.nuanyou.cms.dao.CityDao;
import com.nuanyou.cms.dao.CountryDao;
import com.nuanyou.cms.dao.MissionGroupBdDao;
import com.nuanyou.cms.dao.MissionGroupDao;
import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.entity.MissionGroupBd;
import com.nuanyou.cms.model.MissionGroupVo;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.service.MissionGroupService;
import com.nuanyou.cms.util.BeanUtils;

import org.apache.commons.collections.CollectionUtils;
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
    private MissionGroupDao groupDao;
    
    @Autowired
    private MissionGroupBdDao groupBdDao;
    
    @Autowired
    private CountryDao countryDao;
    
    @Autowired
    private BdCountryDao bdCountryDao;
    
    @Autowired
    private CityDao cityDao;
    
    @Autowired
    private BdUserDao bdUserDao;
    
    @Override
    public Page<MissionGroupVo> findAllGroups(MissionGroupVo requestVo) {
        //分页请求
        final Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());
        
        List<MissionGroup> groups = groupDao.findAll();
        
        List<MissionGroupVo> allCate = convertToBdUserManagerVo(groups);
        
        Page<MissionGroupVo> pageVOs = new PageImpl<>(allCate, pageable, groups.size());
        
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
    public void saveGroup(MissionGroupParamVo vo) {
        MissionGroup group = new MissionGroup(vo.getName(), countryDao.findOne(vo.getCountryId()), cityDao.findOne(vo.getCityId()));
        group.setIsPublic(Byte.parseByte(vo.getIsPublic()));
        group.setDesc(vo.getDesc());
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
    
    @Override
    public List<BdUser> findBdUsersByGroupId(Long groupId) {
        List<MissionGroupBd> groupUsers = groupBdDao.findByGroupId(groupId);
        List<Long> userIds = Lists.newArrayList();
        for (MissionGroupBd groupUser : groupUsers) {
            userIds.add(groupUser.getBdUserId());
        }
        return bdUserDao.findByIdIn(userIds);
    }
    
    @Override
    public MissionGroup findGroupByUserId(Long userId) {
        return null;
    }
    
    private List<MissionGroupVo> convertToBdUserManagerVo(List<MissionGroup> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Lists.newArrayList();
        }
        List<MissionGroupVo> vos = Lists.newArrayList();
        for (MissionGroup group : groups) {
            MissionGroupVo managerVo = BeanUtils.copyBean(group, new MissionGroupVo());
            vos.add(managerVo);
        }
        return vos;
    }
}
