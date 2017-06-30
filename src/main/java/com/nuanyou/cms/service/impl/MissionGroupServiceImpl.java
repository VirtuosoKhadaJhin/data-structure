package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.model.MissionGroupVo;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
    private CityDao cityDao;
    
    @Autowired
    private BdUserDao bdUserDao;
    
    @Override
    public Page<MissionGroupVo> findAllGroups(MissionGroupVo requestVo) {
        //分页请求
        final Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());
        
        List<MissionGroup> groups = groupDao.findAllGroup();
        
        List<MissionGroupVo> allCate = convertToBdUserManagerVo(groups);
        
        Page<MissionGroupVo> pageVOs = new PageImpl<>(allCate, pageable, groups.size());
        
        return pageVOs;
    }
    
    @Override
    public List<Country> findAllCountries() {
        List<Country> countries = countryDao.findAll();
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
    public void updateGroup(String id, MissionGroupParamVo vo) {
        MissionGroup group = groupDao.findOne(Long.valueOf(id));
        
        group.setName(vo.getName());
        group.setCountry(countryDao.findOne(vo.getCountryId()));
        group.setCity(cityDao.findOne(vo.getCityId()));
        group.setIsPublic(Byte.valueOf(vo.getIsPublic()));
        group.setDesc(vo.getDesc());
    
        groupDao.save(group);
    }
    
    @Override
    public Boolean delGroupById(Long id) {
        //获取group
        MissionGroup group = groupDao.findOne(id);
        
        //设置逻辑删除
        group.setDelFlag(Byte.valueOf("1"));
        
        //保存
        groupDao.save(group);
        
        return true;
    }
    
    @Override
    public List<BdUser> findBdUserSByCountryId(Long countryId) {
        List<BdUser> bdUsers = bdUserDao.findBdUsersByCountryId(countryId);
        return bdUsers;
    }
    
    @Override
    public Boolean addGroupBdUser(Long[] bDUserIds, Long groupId) {
        for (Long userId : bDUserIds) {
            //新建groupBd
            MissionGroupBd groupBd = new MissionGroupBd();
            
            //设置数据
            groupBd.setGroupId(groupId);
            groupBd.setBdId(userId);
    
            //保存
            groupBdDao.save(groupBd);
        }
        
        return true;
    }

    @Override
    public void updateGroupPublic(Long groupId, boolean isPublic) {
        groupDao.updatePublicByGroupId(groupId,isPublic);
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
    
    // TODO: 2017/6/29 可能有误，单个还是多个
    @Override
    public MissionGroup findGroupByUserId(Long userId) {
        List<MissionGroupBd> groupBds = groupBdDao.findByBdId(userId);
        Long groupId = groupBds.get(0).getGroupId();
        MissionGroup group = groupDao.findOne(groupId);
        
        return group;
    }
    
    // TODO: 2017/6/29 可能有误，单个还是多个
    @Override
    public List<BdUser> findBdUsersByGroupId(Long groupId) {
        List<MissionGroupBd> groupBds = groupBdDao.findByGroupId(groupId);
    
        List<Long> userIds = Lists.newArrayList();
        for (MissionGroupBd groupBd : groupBds) {
            userIds.add(groupBd.getBdId());
        }
        return bdUserDao.findByIdIn(userIds);
    }
    
    @Override
    public Boolean updateIsPublicByGroupId(Long id) {
        //获取数据
        MissionGroup group = groupDao.findOne(id);
        
        //设置数据
        if (group.getIsPublic() == 0) {
            group.setIsPublic(Byte.valueOf("1"));
        } else {
            group.setIsPublic(Byte.valueOf("0"));
        }
        
        //保存数据
        groupDao.save(group);
        
        return true;
    }
    
    @Override
    public MissionGroup findGroupById(Long id) {
        MissionGroup group = groupDao.findOne(id);
        return group;
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
