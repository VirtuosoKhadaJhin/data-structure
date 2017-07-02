package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.model.BdUserManagerRequestVo;
import com.nuanyou.cms.model.BdUserParamVo;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.BdUserManagerService;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    private MissionGroupBdDao groupBdDao;

    @Autowired
    private BdRoleDao bdRoleDao;

    @Autowired
    private BdRelUserRoleDao bdRelUserRoleDao;

    @Autowired
    private CountryService countryService;

    @Autowired
    private BdCountryDao bdCountryDao;


    @Override
    public Page<BdUserVo> findAllBdUserVos(final BdUserManagerRequestVo requestVo) {
        // TODO: 2017/6/22 Spring data， Spring data jpa需要学习

        //分页请求
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, PageUtil.pageSize);

        //配置查询条件,查询表中数据
        Page<BdUser> bdUsers = bdUserDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.equal(root.get("deleted"), 0));

                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        }, pageable);

//        List<BdRole> bdRoles = bdRoleDao.findAll();

        //应该使用联合查询，
        List<BdRelUserRole> bdRelUserRoles = bdRelUserRoleDao.findAll();

        //        List<BdUserVo> allCate = this.convertToBdUserManagerVo(bdUsers, bdRoles, bdRelUserRoles);
        List<BdUserVo> allCate = this.convertToBdUserManagerVo(bdUsers.getContent(), bdRelUserRoles);

        Page<BdUserVo> pageVOs = new PageImpl<>(allCate, pageable, bdUsers.getTotalElements());

        return pageVOs;
    }

    @Override
    public List<BdRole> findAllRoles() {
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
    public BdUserVo findUserById(Long id) {
        List<BdRelUserRole> userRoles = bdRelUserRoleDao.findAll();
        BdUser user = bdUserDao.findUserById(id);

        BdUserVo vo = new BdUserVo();
        vo.setId(user.getId());
        vo.setName(user.getName());
        vo.setChineseName(user.getChineseName());

        //设置国家
        vo.setCountry(countryService.findOne(user.getCountryId()));
        vo.setEmail(user.getEmail());
        vo.setDmail(user.getDmail());
        vo.setDeleted(user.getDeleted());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());

        //判断用户是否有角色
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
    public void saveAddUserAndRole(BdUserParamVo paramVo) {
        BdUser user = new BdUser();
        BdRelUserRole userRole = new BdRelUserRole();

        //保存用户信息
        user.setName(paramVo.getName());
        user.setChineseName(paramVo.getChineseName());
        user.setEmail(paramVo.getEmail());
        user.setDmail(paramVo.getDmail());

        //设置默认密码
        String pwd = MD5Utils.encrypt("123456");
        user.setPwd(pwd);

        //设置默认显示
        user.setDeleted(Byte.valueOf("0"));

        //保存用户角色信息
        userRole.setUser(user);
        BdRole role = this.findRoleById(paramVo.getRoleId());

        userRole.setRole(role);

        this.saveUser(user);
        this.saveUserRole(userRole);
    }

    @Override
    public void saveEditUserAndRole(BdUserParamVo paramVo) {
        //获取数据
        BdUser user = this.findBdUserById(paramVo.getId());
        BdRelUserRole userRole = new BdRelUserRole();
        BdRole role = this.findRoleById(paramVo.getRoleId());

        //设置数据
        user.setName(paramVo.getName());
        user.setChineseName(paramVo.getChineseName());
        user.setEmail(paramVo.getEmail());
        user.setDmail(paramVo.getDmail());

        //保存用户角色信息
        userRole.setUser(user);
        userRole.setRole(role);

        //保存数据
        this.updateUser(user);
        this.updateUserRole(userRole);
    }

    @Override
    public void del(Long id) {
        BdUser user = bdUserDao.findOne(id);
        user.setDeleted(Byte.valueOf("1"));

        //保存信息
        bdUserDao.save(user);
    }

    @Override
    public BdUser findBdUserByDemail(String email) {
        return bdUserDao.findOne(Example.of(new BdUser(email)));
    }

    @Override
    public List<BdUser> findByGroupId(Long groupId) {
        List<MissionGroupBd> missionGroupBds = groupBdDao.findByGroupId(groupId);
        List<Long> bdUserIds = Lists.newArrayList();
        for (MissionGroupBd groupBd : missionGroupBds) {
            bdUserIds.add(groupBd.getBdId());
        }
        // 一次性查询所有用户
        List<BdUser> bdUsers = bdUserDao.findAll(bdUserIds);
        return bdUsers;
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
     *
     * @param users
     * @param userRoles
     * @return
     */
    private List<BdUserVo> convertToBdUserManagerVo(List<BdUser> users, List<BdRelUserRole> userRoles) {

        ArrayList<BdUserVo> list = new ArrayList<>();
        for (BdUser user : users) {
            BdUserVo vo = new BdUserVo();
            vo.setId(user.getId());
            vo.setName(user.getName());
            vo.setChineseName(user.getChineseName());
            vo.setCountry(countryService.findOne(user.getCountryId()));
            vo.setEmail(user.getEmail());
            vo.setDmail(user.getDmail());
            vo.setDeleted(user.getDeleted());
            vo.setCreateTime(user.getCreateTime());
            vo.setUpdateTime(user.getUpdateTime());

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
        List<BdUser> bdUsers = bdUserDao.findallBdUser();
        return bdUsers;
    }

}