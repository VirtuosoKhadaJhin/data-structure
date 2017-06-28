package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.MissionGroupManagerVo;
import com.nuanyou.cms.model.MissionGroupParamVo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 战队用户管理
 * <p>
 * Created by sharp on 2017/6/28 - 15:55
 */
public interface MissionGroupService {
    
    /**
     * 查找所有的组信息
     *
     * @param requestVo
     * @return
     */
    Page<MissionGroupManagerVo> findAllGroups(MissionGroupManagerVo requestVo);
    
    List<BdCountry> findAllCountries();
    
    List<City> findAllCities();
    
    /**
     * 保存战队信息
     *
     * @param vo
     */
    void saveGroup(MissionGroupParamVo vo);
    
    /**
     * 按照国家id或者城市id查询战队
     *
     * @param countryId
     * @param cityId
     * @return
     */
    List<MissionGroup> findByCountryAndCityId(Long countryId, Long cityId);
    
    /**
     * 通过groupId查找该组的所有bduser
     *
     * @param groupId
     * @return
     */
    List<BdUser> findBdUsersByGroupId(Long groupId);
}
