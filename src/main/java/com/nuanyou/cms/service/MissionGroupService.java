package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.MissionGroupManagerVo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 战队用户管理
 *
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
     * @param group
     */
    void saveGroup(MissionGroup group);
}
