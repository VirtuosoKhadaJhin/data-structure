package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.model.mission.MissionGroupParamVo;
import com.nuanyou.cms.model.mission.MissionGroupVo;
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
    Page<MissionGroupVo> findAllGroups(final MissionGroupVo requestVo);

    /**
     * 保存战队信息
     *
     * @param vo
     */
    void saveGroup(MissionGroupParamVo vo);

    /**
     * 按照国家查询战队
     *
     * @param country
     * @return
     */
    List<MissionGroup> findByCountry(Long country);

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
     * 通过id找到组
     *
     * @param id
     * @return
     */
    MissionGroup findGroupById(Long id);

    /**
     * 更新组
     *
     * @param id
     * @param paramVo
     */
    void updateGroup(Long id, MissionGroupParamVo paramVo);

    /**
     * 通过id删除组
     *
     * @param id
     * @return
     */
    Boolean delGroupById(Long id);

    /**
     * 查询可分配组的bdUser（没有组的BdUser）
     *
     * @param countryId
     * @param groupId
     * @return
     */
    List<BdUser> findNonGroupByCountryId(Long countryId, Long groupId);

    /**
     * 查询所有国家id可分配组的bdUser（没有组的BdUser）
     *
     * @return
     */
    List<BdUser> findAllBdUserNonGroup();

    /**
     * 更新组是否可见
     *
     * @param groupId
     * @param isPublic
     */
    void updateGroupPublic(Long groupId, boolean isPublic);

    /**
     * 添加组成员
     *
     * @param groupId
     * @param bdIds
     */
    void saveGroupBds(Long groupId, List<Long> bdIds);

    /**
     * 根据组Id查询用户
     *
     * @param groupId
     * @return
     */
    List<Long> findBdUserByGroupId(Long groupId);

    /**
     * 查询已有的组员(指派队长)
     *
     * @param id
     * @param countryId
     * @return
     */
    List<BdUserVo> members(Long id, Long countryId);

    /**
     * 指派队长
     *
     * @param groupId
     * @param leaderId
     * @return
     */
    Boolean distributeLeader(Long groupId, Long leaderId);

    /**
     * 校验战队名称是否唯一
     *
     * @param groupId
     * @param name
     * @return
     */
    List<MissionGroup> checkGroupUnique(Long groupId, String name);
}
