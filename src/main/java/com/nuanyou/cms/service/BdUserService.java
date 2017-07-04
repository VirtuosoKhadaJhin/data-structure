package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRelUserRole;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.model.BdUserRequestVo;
import com.nuanyou.cms.model.BdUserParamVo;
import com.nuanyou.cms.model.BdUserVo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * bd宝用户管理
 * <p>
 * Created by sharp on 2017/6/22 - 15:03
 */
public interface BdUserService {
    /**
     * //     * 查询所有bd宝用户列表
     *
     * @return bd宝用户列表
     */
    Page<BdUserVo> findAllBdUserVos(final BdUserRequestVo requestVo);

    /**
     * 查询所有的用户角色
     *
     * @return 用户角色列表
     */
    List<BdRole> findAllRoles();

    /**
     * 查询所有的国家
     *
     * @return 返回所有的国家列表
     */
    List<BdCountry> findAllCountry();

    /**
     * 保存bd宝用户信息
     *
     * @param user bd宝用户信息
     */
    BdUser saveUser(BdUser user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    void updateUser(BdUser user);

    /**
     * 保存用户角色信息
     *
     * @param userRole 用户角色
     */
    void saveUserRole(BdRelUserRole userRole);

    BdUserVo findUserById(Long id);

    BdRole findRoleById(Long roleId);

    /**
     * 更新角色
     *
     * @param userRole
     */
    void updateUserRole(BdRelUserRole userRole);

    BdUser findBdUserById(long id);

    List<BdUser> findAllBdUsers();

    /**
     * 保存新增的bduser和角色信息
     *
     * @param paramVo
     */
    void saveAddUserAndRole(BdUserParamVo paramVo);

    /**
     * 保存编辑的bduser和角色信息
     *
     * @param paramVo
     */
    void saveEditUserAndRole(BdUserParamVo paramVo);

    /**
     * 删除bduser信息
     *
     * @param aLong
     */
    void del(Long aLong);

    /**
     * 根据email查询BDUser
     *
     * @param email
     * @return
     */
    BdUser findBdUserByDemail(String email);

    // List<BdUser> findBdUsersByCountryId(Long );

    /**
     * 根据组Id查询组员列表
     *
     * @param groupId
     * @return
     */
    List<BdUser> findByGroupId(Long groupId);

    /**
     * 获取bduser列表
     *
     * @param country
     * @param groupId
     * @return
     */
    List<BdUser> findByCountryAndGroup(Long country, Long groupId);
}
