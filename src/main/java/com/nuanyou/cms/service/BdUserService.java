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

public interface BdUserService {
    /**
     * 根据条件搜索BD用户
     *
     * @param requestVo
     * @return
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

    /**
     * 查询角色信息
     *
     * @param roleId
     * @return
     */
    BdRole findRoleById(Long roleId);

    /**
     * 更新角色
     *
     * @param userRole
     */
    void updateUserRole(BdRelUserRole userRole);

    /**
     * 查询用户信息（带角色）
     *
     * @param id
     * @return
     */
    BdUserVo findUserById(Long id);

    /**
     * 查询用户信息（不带角色）
     *
     * @param id
     * @return
     */
    BdUser findBdUserById(long id);

    /**
     * 查询所有BD用户
     *
     * @return
     */
    List<BdUser> findAllBdUsers();

    /**
     * 查询组所有成员
     *
     * @param groupId
     * @return
     */
    List<BdUser> findBdUsersByGroup(Long groupId);

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

    /**
     * 校验BD用户名是否唯一
     *
     * @param id
     * @param name
     * @return
     */
    Boolean checkBdUserUnique(Long id, String name);

    /**
     * 校验BD钉钉邮箱是否唯一
     *
     * @param id
     * @param dmail
     * @return
     */
    Boolean checkDmailUnique(Long id, String dmail);
}
