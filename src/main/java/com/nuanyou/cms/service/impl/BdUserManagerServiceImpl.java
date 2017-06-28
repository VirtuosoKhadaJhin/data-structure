package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.BdCountryDao;
import com.nuanyou.cms.dao.BdRelUserRoleDao;
import com.nuanyou.cms.dao.BdRoleDao;
import com.nuanyou.cms.dao.BdUserDao;
import com.nuanyou.cms.entity.BdCountry;
import com.nuanyou.cms.entity.BdRelUserRole;
import com.nuanyou.cms.entity.BdRole;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserManagerVo;
import com.nuanyou.cms.service.BdUserManagerService;
import com.nuanyou.cms.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * bd宝用户管理
 * <p>
 * Created by sharp on 2017/6/22 - 15:04
 */
@Service
public class BdUserManagerServiceImpl implements BdUserManagerService {
    
    @Autowired
    private BdUserDao bdUserDao;
    
    @Autowired
    private BdRoleDao bdRoleDao;
    
    @Autowired
    private BdRelUserRoleDao bdRelUserRoleDao;
    
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private BdCountryDao bdCountryDao;
    
    
    //    private BdUserDao bdUserDao;
    @Override
    public Page<BdUserManagerVo> findAllBdUserManagerVos(final BdUserManagerRequestVo requestVo) {
        // TODO: 2017/6/22 Spring data， Spring data jpa需要学习
        
        //分页请求
        final Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());
        
        //配置查询条件,查询表中数据
        List<BdUser> bdUsers = bdUserDao.findAll();
//        List<BdRole> bdRoles = bdRoleDao.findAll();
        
        //应该使用联合查询，
        List<BdRelUserRole> bdRelUserRoles = bdRelUserRoleDao.findAll();
        
        //        List<BdUserManagerVo> allCate = this.convertToBdUserManagerVo(bdUsers, bdRoles, bdRelUserRoles);
        List<BdUserManagerVo> allCate = this.convertToBdUserManagerVo(bdUsers, bdRelUserRoles);
        
        Page<BdUserManagerVo> pageVOs = new PageImpl<>(allCate, pageable, bdUsers.size());
        
        return pageVOs;
    }
    
    @Override
    public List<BdRole> findAllRole() {
        List<BdRole> bdRoles = bdRoleDao.findAll();
        return bdRoles;
    }
    
    @Override
    public List<BdCountry> findAllCountry() {
        List<BdCountry> countries = bdCountryDao.findAll();
        return countries;
    }
    
    
    @Override
    public BdUser saveUser(BdUser user) {
        BdUser newUser = bdUserDao.save(user);
        return newUser;
    }
    
    
    @Override
    public void updateUser(BdUser user) {
        bdUserDao.save(user);
    }
    
    @Override
    public void saveUserRole(BdRelUserRole userRole) {
        bdRelUserRoleDao.save(userRole);
    }
    
    @Override
    public BdUserManagerVo findUserById(Long id) {
        List<BdRelUserRole> userRoles = bdRelUserRoleDao.findAll();
        BdUser user = bdUserDao.findUserById(id);
        
        BdUserManagerVo vo = new BdUserManagerVo();
        vo.setUser(user);
        Country country = countryService.findOne(user.getCountryId());
        vo.setCountry(country);
        for (BdRelUserRole userRole : userRoles) {
            if (user.equals(userRole.getUser())) {
                vo.setRole(userRole.getRole());
            }
        }
        
        return vo;
    }
    
    @Override
    public BdRole findRoleById(Long roleId) {
        BdRole role = bdRoleDao.findOne(roleId);
        return role;
    }
    
    @Override
    public void updateUserRole(BdRelUserRole userRole) {
        List<BdRelUserRole> userRoles = bdRelUserRoleDao.findAll();
        for (BdRelUserRole relUserRole : userRoles) {
            //找到数据库对应的条目，修改，更新
            if (relUserRole.getUser().getId().equals(userRole.getUser().getId())) {
                //修改
                relUserRole.setRole(userRole.getRole());
                //更新
                bdRelUserRoleDao.save(relUserRole);
            }
        }
    }
    
    
    /**
     * 用户可能没有角色，因此要以用户为主体，
     * @param users
     * @param userRoles
     * @return
     */
    private List<BdUserManagerVo> convertToBdUserManagerVo(List<BdUser> users, List<BdRelUserRole> userRoles) {
    
        ArrayList<BdUserManagerVo> list = new ArrayList<>();
        for (BdUser user : users) {
            BdUserManagerVo vo = new BdUserManagerVo();
            vo.setUser(user);
    
            //设置国家
            Country country = countryService.findOne(user.getCountryId());
            vo.setCountry(country);
    
            //判断用户是否有角色
            for (BdRelUserRole userRole : userRoles) {
                if (user.equals(userRole.getUser())) {
                    vo.setRole(userRole.getRole());
                }
            }
    
            list.add(vo);
        }
    
        return list;
    }
    
    private String generateRoleNameByRoles(List<BdRelUserRole> roles) {
        StringBuffer stringBuffer = new StringBuffer();
        for (BdRelUserRole role : roles) {
            if (stringBuffer.toString().equals("")) {
                stringBuffer.append(role.getRole().getDesc());
            } else {
                stringBuffer.append("，" + role.getRole().getDesc());
            }
    
        }
        return stringBuffer.toString();
    }
    
    @Override
    public BdUser findBdUserById(long id) {
        return this.bdUserDao.findOne(id);
    }
    
    @Override
    public List<BdUser> findAllBdUsers() {
        List<BdUser> bdUsers = bdUserDao.findAll();
        return bdUsers;
    }
}