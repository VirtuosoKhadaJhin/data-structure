package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.City;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.MissionGroup;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.model.MissionGroupVo;

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
    Page<MissionGroupVo> findAllGroups(MissionGroupVo requestVo);
    
    List<Country> findAllCountries();
    
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
    
    /**
     * 查询用户所在组
     *
     * @param userId
     * @return
     */
    MissionGroup findGroupByUserId(Long userId);
    
    /**
     * 根据groupId更新任务对bduser可见
     *
     * @param id
     * @return
     */
    Boolean updateIsPublicByGroupId(Long id);
    
    /**
     * 通过id找到组
     *
     * @param id
     * @return
     */
    MissionGroup findGroupById(Long id);
    
    void updateGroup(String id, MissionGroupParamVo paramVo);
    
    /**
     * 通过id删除组
     *
     * @param id
     * @return
     */
    Boolean delGroupById(Long id);
    
    List<BdUser> findBdUserSByCountryId(Long countryId);
    
    /**
     * 给战队添加成员
     *
     * @param bDUserIds
     * @param groupId
     * @return
     */
    Boolean addGroupBdUser(Long[] bDUserIds, Long groupId);
}
