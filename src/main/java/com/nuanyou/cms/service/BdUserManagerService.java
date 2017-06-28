package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRelUserRole;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserManagerVo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * bd宝用户管理
 * <p>
 * Created by sharp on 2017/6/22 - 15:03
 */
public interface BdUserManagerService {
    /**
//     * 查询所有bd宝用户列表
     *
     * @return bd宝用户列表
     */
    Page<BdUserManagerVo> findAllBdUsers(final BdUserManagerRequestVo requestVo);
    
    /**
     * 查询所有的用户角色
     *
     * @return 用户角色列表
     */
    List<BdRole> findAllRole();
    
    /**
     * 查询所有的国家
     *
     * @return 返回所有的国家列表
     */
    List<BdCountry> findAllCountry();
    
    /**
     * 通过id找到用户信息
     *
     * @param id 用户id
     *
     * @return 用户信息
     */
//    BdUserManagerVo findUserById(Long id);
    
    /**
     * 保存bd宝用户信息
     *
     * @param user bd宝用户信息
     */
    BdUser saveUser(BdUser user);
    
    
    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void updateUser(BdUser user);
    
    /**
     * 保存用户角色信息
     *
     * @param userRole 用户角色
     */
    void saveUserRole(BdRelUserRole userRole);
    
    BdUserManagerVo findUserById(Long id);
    
    BdRole findRoleById(Long roleId);
    
    /**
     * 更新角色
     *
     * @param userRole
     */
    void updateUserRole(BdRelUserRole userRole);
    
    BdUser findBdUserById(long id);
}
