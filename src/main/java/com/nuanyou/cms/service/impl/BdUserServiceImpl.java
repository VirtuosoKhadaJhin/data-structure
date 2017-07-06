package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.entity.mission.MissionGroupBd;
import com.nuanyou.cms.model.BdUserParamVo;
import com.nuanyou.cms.model.BdUserRequestVo;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.BdUserService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.MD5Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BdUserServiceImpl implements BdUserService {

    @Autowired
    private BdUserDao bdUserDao;

    @Autowired
    private MissionGroupBdDao groupBdDao;

    @Autowired
    private MissionGroupDao groupDao;

    @Autowired
    private BdRoleDao bdRoleDao;

    @Autowired
    private BdRelUserRoleDao bdRelUserRoleDao;

    @Autowired
    private BdCountryDao bdCountryDao;

    @Override
    public Page<BdUserVo> findAllBdUserVos(final BdUserRequestVo requestVo) {
        //分页请求
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, PageUtil.pageSize);

        //配置查询条件,查询表中数据
        Page<BdUser> bdUsers = bdUserDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.equal(root.get("deleted"), 0));
                if (requestVo.getConturyid() != null) {
                    predicate.add(cb.equal(root.get("country").get("id"), requestVo.getConturyid()));
                }
                if (StringUtils.isNotEmpty(requestVo.getName())) {
                    predicate.add(cb.like(root.get("name"), "%" + requestVo.getName() + "%"));
                }
                if (StringUtils.isNotEmpty(requestVo.getChineseName())) {
                    predicate.add(cb.like(root.get("chineseName"), "%" + requestVo.getChineseName() + "%"));
                }
                if (StringUtils.isNotEmpty(requestVo.getDmail())) {
                    predicate.add(cb.equal(root.get("dmail"), requestVo.getDmail()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("updateTime")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        }, pageable);

        List<BdUserVo> allCate = this.convertToBdUserManagerVo(bdUsers.getContent());
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
        BdRelUserRole relUserRole = bdRelUserRoleDao.findByUserId(id);
        BdUser user = relUserRole.getUser();
        BdUserVo bdUserVo = BeanUtils.copyBean(user, new BdUserVo());
        bdUserVo.setRole(relUserRole.getRole());
        return bdUserVo;
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
        user.setName(paramVo.getName());
        user.setChineseName(paramVo.getChineseName());
        user.setEmail(paramVo.getEmail());
        user.setDmail(paramVo.getDmail());
        user.setCountry(new Country(paramVo.getCountryId()));
        String pwd = MD5Utils.encrypt("123456");
        user.setPwd(pwd);
        user.setDeleted(Byte.valueOf("0"));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userRole.setUser(user);
        BdRole role = this.findRoleById(paramVo.getRoleId());
        userRole.setRole(new BdRole(paramVo.getRoleId()));
        this.saveUser(user);
        this.saveUserRole(userRole);
    }

    @Override
    public void saveEditUserAndRole(BdUserParamVo paramVo) {
        Long bdUserId = paramVo.getId();
        BdUser user = this.findBdUserById(bdUserId);
        Long oldCountryId = user.getCountry().getId();
        BdRelUserRole userRole = new BdRelUserRole();
        BdRole role = this.findRoleById(paramVo.getRoleId());
        user.setName(paramVo.getName());
        user.setChineseName(paramVo.getChineseName());
        user.setCountry(new Country(paramVo.getCountryId()));
        user.setEmail(paramVo.getEmail());
        user.setDmail(paramVo.getDmail());
        user.setUpdateTime(new Date());
        userRole.setUser(user);
        userRole.setRole(role);
        this.updateUser(user);
        this.updateUserRole(userRole);

        //如果BD用户国家改变，删除组中关系
        this.unbindGroupBdRelation(paramVo, oldCountryId);
    }

    @Override
    public void del(Long id) {
        bdUserDao.updateDeleteUser(id);
        groupBdDao.deleteByBdUserId(id);
        MissionGroup leaderGroup = groupDao.findByLeaderId(id);
        if (leaderGroup != null) {
            leaderGroup.setLeader(null);
            groupDao.save(leaderGroup);
        }
        MissionGroup viceLeaderGroup = groupDao.findByViceLeaderId(id);
        if (viceLeaderGroup != null) {
            viceLeaderGroup.setViceLeader(null);
            groupDao.save(viceLeaderGroup);
        }
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
    public List<BdUser> findByCountryAndGroup(final Long country, final Long groupId) {
        List<BdUser> result = Lists.newArrayList();
        List<BdUser> countryUsers = bdUserDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.equal(root.get("deleted"), 0));
                if (country != null) {
                    predicate.add(cb.equal(root.get("country").get("id"), country));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.asc(root.get("updateTime")));
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });
        if ((country != null && groupId == null) || (country == null || groupId == null)) {
            return countryUsers;
        } else if (country == null && groupId != null) {
            return this.findBdUsersByGroup(groupId);
        } else if (country != null && groupId != null) {
            Map<Long, BdUser> bdUserMaps = new LinkedHashMap<Long, BdUser>(countryUsers.size());
            for (BdUser bdUser : countryUsers) {
                bdUserMaps.put(bdUser.getId(), bdUser);
            }
            List<BdUser> groupUsers = findBdUsersByGroup(groupId);
            //合并groupUsers和countryUsers同时存在的
            Iterator<BdUser> iterator = groupUsers.iterator();
            while (iterator.hasNext()) {
                BdUser bdUser = iterator.next();
                if (bdUserMaps.containsKey(bdUser.getId())) {
                    result.add(bdUser);
                }
            }
        }
        return result;
    }

    @Override
    public Boolean checkBdUserUnique(Long id, String name) {
        if (id == null) {
            return bdUserDao.checkNameRepeat(name).size() > 0;
        }
        return bdUserDao.findByNameNonBdUser(id, name).size() > 0;
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

    @Override
    public BdUser findBdUserById(long id) {
        return this.bdUserDao.findOne(id);
    }

    @Override
    public List<BdUser> findAllBdUsers() {
        return bdUserDao.findallBdUser();
    }

    @Override
    public List<BdUser> findBdUsersByGroup(Long groupId) {
        List<MissionGroupBd> userBds = groupBdDao.findByGroupId(groupId);
        Collection<Long> userIds = (List<Long>) CollectionUtils.collect(userBds, new Transformer() {
            @Override
            public Long transform(Object input) {
                return ((MissionGroupBd) input).getBdId();
            }
        });
        return bdUserDao.findByIdIn(Lists.newArrayList(userIds));
    }

    /**
     * 用户所属国家改变，解绑组关系
     *
     * @param paramVo
     * @param oldCountryId
     */
    private void unbindGroupBdRelation(BdUserParamVo paramVo, Long oldCountryId) {
        if (oldCountryId.equals(paramVo.getCountryId())) {//国家未改变
            return;
        }
        Long bdUserId = paramVo.getId();
        MissionGroupBd missionGroupBd = groupBdDao.findByBdId(bdUserId);
        if (missionGroupBd == null) {//用户不属于任何组
            return;
        }
        groupBdDao.deleteByBdUserId(bdUserId);
        MissionGroup groupInfo = groupDao.findByGroupId(missionGroupBd.getGroupId());
        if (groupInfo == null) {//组信息不存在或者已删除
            return;
        }
        MissionGroup leaderGroup = groupDao.findByLeaderId(bdUserId);
        MissionGroup viceLeaderGroup = groupDao.findByViceLeaderId(bdUserId);
        if (leaderGroup == null && viceLeaderGroup == null) {
            return;//bdUserId不是队长或者副队长
        }
        if (leaderGroup != null) {
            groupInfo.setLeader(null);
        } else if (viceLeaderGroup != null) {
            groupInfo.setViceLeader(null);
        }
        groupDao.save(groupInfo);
    }

    /**
     * 用户可能没有角色，因此要以用户为主体，
     *
     * @param users
     * @return
     */
    private List<BdUserVo> convertToBdUserManagerVo(List<BdUser> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Lists.newArrayList();
        }
        final Collection userIds = CollectionUtils.collect(users, new Transformer() {
            @Override
            public Long transform(Object input) {
                return ((BdUser) input).getId();
            }
        });
        List<BdRelUserRole> bdUserRoles = bdRelUserRoleDao.findByUserIds(userIds);
        Map<Long, BdRole> maps = new LinkedHashMap<Long, BdRole>(bdUserRoles.size());
        for (BdRelUserRole bdUserRole : bdUserRoles) {
            maps.put(bdUserRole.getUser().getId(), bdUserRole.getRole());
        }
        List<BdUserVo> userVos = Lists.newArrayList();
        for (BdUser user : users) {
            BdUserVo bdUserVo = BeanUtils.copyBean(user, new BdUserVo());
            if (maps.containsKey(user.getId())) {
                bdUserVo.setRole(maps.get(user.getId()));
            }
            userVos.add(bdUserVo);
        }
        return userVos;
    }
}