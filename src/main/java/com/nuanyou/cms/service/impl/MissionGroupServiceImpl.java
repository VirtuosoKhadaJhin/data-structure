package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.model.BdUserVo;
import com.nuanyou.cms.model.MissionGroupParamVo;
import com.nuanyou.cms.model.MissionGroupVo;
import com.nuanyou.cms.service.MissionGroupService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by sharp on 2017/6/28 - 15:56
 */
@Service
public class MissionGroupServiceImpl implements MissionGroupService {

    @Autowired
    private MissionGroupDao groupDao;

    @Autowired
    private MissionGroupBdDao groupBdDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private BdUserDao bdUserDao;

    @Override
    public Page<MissionGroupVo> findAllGroups(final MissionGroupVo requestVo) {
        //分页请求
        final Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());

        Page<MissionGroup> groups = groupDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.equal(root.get("delFlag"), 0));

                if (StringUtils.isNotEmpty(requestVo.getName())) {
                    predicate.add(cb.like(root.get("name"), "%" + requestVo.getName() + "%"));
                }
                if (requestVo.getIsPublic() != null) {
                    predicate.add(cb.equal(root.get("isPublic"), requestVo.getIsPublic()));
                }
                if (requestVo.getCountry() != null) {
                    predicate.add(cb.equal(root.get("country").get("id"), requestVo.getCountry().getId()));
                }
                if (requestVo.getCity() != null) {
                    predicate.add(cb.equal(root.get("city").get("id"), requestVo.getCity().getId()));
                }

                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        },pageable);

        List<MissionGroupVo> groupVos = this.getMissionGroupVos(groups.getContent());
        Page<MissionGroupVo> pageVOs = new PageImpl<>(groupVos, pageable, groups.getTotalElements());
        return pageVOs;
    }

    @Override
    public void saveGroup(MissionGroupParamVo vo) {
        MissionGroup group = new MissionGroup(vo.getName(), countryDao.findOne(vo.getCountryId()), cityDao.findOne(vo.getCityId()));
        group.setDesc(vo.getDesc());
        group.setIsPublic((byte) 0);
        group.setLeader(bdUserDao.findUserById(vo.getLeaderId()));
        group.setViceLeader(bdUserDao.findUserById(vo.getViceLeaderId()));
        MissionGroup missionGroup = groupDao.save(group);

        List<MissionGroupBd> groupBds = Lists.newArrayList();
        groupBds.add(new MissionGroupBd(missionGroup.getId(), vo.getLeaderId()));
        groupBds.add(new MissionGroupBd(missionGroup.getId(), vo.getViceLeaderId()));
        groupBdDao.save(groupBds);
    }

    @Override
    public void updateGroup(Long id, MissionGroupParamVo vo) {
        MissionGroup group = groupDao.findOne(id);
        Long oldLeader = null;
        Long oldViceLeader = null;
        if (group.getLeader() != null) {
            oldLeader = group.getLeader().getId();
        }
        if (group.getViceLeader() != null) {
            oldViceLeader = group.getViceLeader().getId();
        }
        group.setName(vo.getName());
        group.setCountry(countryDao.findOne(vo.getCountryId()));
        group.setCity(cityDao.findOne(vo.getCityId()));
        group.setIsPublic((byte) 0);
        group.setLeader(bdUserDao.findOne(vo.getLeaderId()));
        group.setViceLeader(bdUserDao.findUserById(vo.getViceLeaderId()));
        group.setDesc(vo.getDesc());
        groupDao.save(group);
        if (oldLeader != null && oldViceLeader != null) {
            ArrayList<Long> oldLeaders = Lists.newArrayList(oldLeader, oldViceLeader);
            groupBdDao.deleteOldLeaders(oldLeaders);
        }
        ArrayList<MissionGroupBd> missionGroupBds = Lists.newArrayList(new MissionGroupBd(group.getId(), vo.getLeaderId()), new MissionGroupBd(group.getId(), vo.getViceLeaderId()));
        groupBdDao.save(missionGroupBds);
    }

    @Override
    public Boolean delGroupById(Long id) {
        groupDao.deleteGroup(id);
        groupBdDao.deleteByGroupId(id);
        return true;
    }

    @Override
    public List<BdUser> findNonGroupByCountryId(Long countryId, Long groupId) {
        List<BdUser> bdUsers = bdUserDao.findBdUsersByCountryId(countryId);

        // 查询联合表, 不需要已经有组的组员了!
        if (groupId == null) {
            List<MissionGroupBd> missionGroupBds = groupBdDao.findAll();
            swichUserNoGroup(missionGroupBds, bdUsers);
            return bdUsers;
        }
        //查询不是当前组的队员
        List<MissionGroupBd> missionGroupBds = groupBdDao.findByNonGroupId(groupId);
        swichUserNoGroup(missionGroupBds, bdUsers);
        return bdUsers;
    }

    @Override
    public List<BdUser> findAllBdUserNonGroup() {
        List<BdUser> bdUsers = bdUserDao.findAll();

        // 查询联合表, 不需要已经有组的组员了!
        List<MissionGroupBd> missionGroupBds = groupBdDao.findAll();
        swichUserNoGroup(missionGroupBds, bdUsers);
        return bdUsers;
    }

    @Override
    public void updateGroupPublic(Long groupId, boolean isPublic) {
        byte b = (byte) (isPublic ? 1 : 0);
        groupDao.updatePublicByGroupId(groupId, b);
    }

    @Override
    public void saveGroupBds(Long groupId, List<Long> bdIds) {
        if (groupId == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        List<MissionGroupBd> groupBds = Lists.newArrayList();
        MissionGroupBd groupBd = null;
        for (Long bdId : bdIds) {
            groupBd = new MissionGroupBd(groupId, bdId);
            groupBds.add(groupBd);
        }
        groupBdDao.deleteByGroupId(groupId);
        if (CollectionUtils.isNotEmpty(groupBds)) {
            groupBdDao.save(groupBds);
        }
    }

    @Override
    public List<Long> findBdUserByGroupId(Long groupId) {
        List<MissionGroupBd> missionGroupBds = groupBdDao.findByGroupId(groupId);
        List<Long> bdUserIds = Lists.newArrayList();
        for (MissionGroupBd groupBd : missionGroupBds) {
            bdUserIds.add(groupBd.getBdId());
        }
        return bdUserIds;
    }

    @Override
    public List<BdUserVo> members(Long id, Long countryId) {
        //根据组ID查询出组
        MissionGroup group = groupDao.findOne(id);
        // 队长
        BdUser leader = group.getLeader();
        // 根据组ID查询出所有组员
        List<MissionGroupBd> missionGroupBds = groupBdDao.findByGroupId(id);
        List<Long> bdUserIds = Lists.newArrayList();
        for (MissionGroupBd groupBd : missionGroupBds) {
            bdUserIds.add(groupBd.getBdId());
        }

        // 一次性查询所有用户
        if (CollectionUtils.isEmpty(bdUserIds)) {
            return null;
        }

        List<BdUser> bdUsers = bdUserDao.findBdUsersByIdsAndCountryId(bdUserIds, countryId);

        List<BdUserVo> bdUsersVo = convertToBdUserVo(bdUsers);

        if (null != leader) {
            for (BdUserVo vo : bdUsersVo) {
                if (vo.getId().equals(leader.getId())) {
                    vo.setIsLeader(true);
                }
            }
        }

        return bdUsersVo;
    }

    @Override
    public Boolean distributeLeader(Long groupId, Long leaderId) {
        // 查询出被设置为leader的用户
        BdUser bdUser = bdUserDao.findOne(leaderId);
        groupDao.updateLeaderByGroupId(bdUser, groupId);
        return true;
    }

    @Override
    public List<MissionGroup> checkGroupUnique(Long groupId, String name) {
        if (groupId == null) {
            return groupDao.findByName(name);
        } else {
            return groupDao.findByNameNonGroup(groupId, name);
        }
    }

    @Override
    public List<MissionGroup> findByCityId(final Long city) {
        if (city == null) {
            return groupDao.findAllGroup();
        }
        return groupDao.findGroupsByCityId(city);
    }

    @Override
    public MissionGroup findGroupByUserId(Long userId) {
        List<MissionGroupBd> groupBds = groupBdDao.findByBdId(userId);
        Long groupId = groupBds.get(0).getGroupId();
        MissionGroup group = groupDao.findOne(groupId);

        return group;
    }

    @Override
    public List<BdUser> findBdUsersByGroupId(Long groupId) {
        List<MissionGroupBd> groupBds = groupBdDao.findByGroupId(groupId);

        List<Long> userIds = Lists.newArrayList();
        for (MissionGroupBd groupBd : groupBds) {
            userIds.add(groupBd.getBdId());
        }
        return bdUserDao.findByIdIn(userIds);
    }

    @Override
    public MissionGroup findGroupById(Long id) {
        MissionGroup group = groupDao.findOne(id);
        return group;
    }

    private List<MissionGroupVo> getMissionGroupVos(List<MissionGroup> groups) {
        List<MissionGroupVo> groupVos = convertToBdUserManagerVo(groups);
        if (CollectionUtils.isEmpty(groupVos)) {
            return groupVos;
        }
        final Map<Long, MissionGroupVo> maps = new LinkedHashMap<Long, MissionGroupVo>();
        for (MissionGroupVo groupVo : groupVos) {
            maps.put(groupVo.getId(), groupVo);
        }
        List<MissionGroupBd> groupBds = groupBdDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (MapUtils.isNotEmpty(maps)) {
                    predicate.add(root.get("groupId").in(maps.keySet()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("groupId")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        });
        Iterator<MissionGroupBd> iterator = groupBds.iterator();
        while (iterator.hasNext()) {
            MissionGroupBd groupBd = iterator.next();
            Long groupId = groupBd.getGroupId();
            if (!maps.containsKey(groupId)) {
                continue;
            }
            MissionGroupVo missionGroupVo = maps.get(groupId);
            missionGroupVo.getbDUserIds().add(groupBd.getBdId());
        }
        return groupVos;
    }

    private List<MissionGroupVo> convertToBdUserManagerVo(List<MissionGroup> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Lists.newArrayList();
        }
        List<MissionGroupVo> vos = Lists.newArrayList();
        for (MissionGroup group : groups) {
            MissionGroupVo managerVo = BeanUtils.copyBean(group, new MissionGroupVo());
            vos.add(managerVo);
        }
        return vos;
    }

    private List<BdUserVo> convertToBdUserVo(List<BdUser> bdUsers) {
        if (CollectionUtils.isEmpty(bdUsers)) {
            return Lists.newArrayList();
        }
        List<BdUserVo> vos = Lists.newArrayList();
        for (BdUser bdUser : bdUsers) {
            BdUserVo bdUserVoVo = BeanUtils.copyBean(bdUser, new BdUserVo());
            vos.add(bdUserVoVo);
        }
        return vos;
    }

    private void swichUserNoGroup(List<MissionGroupBd> missionGroupBds, List<BdUser> bdUsers) {
        List<Long> userHaveGroups = Lists.newArrayList();
        for (MissionGroupBd missionGroupBd : missionGroupBds) {
            userHaveGroups.add(missionGroupBd.getBdId());
        }
        Iterator<BdUser> iterator = bdUsers.iterator();
        while (iterator.hasNext()) {
            BdUser next = iterator.next();
            if (userHaveGroups.contains(next.getId())) {
                iterator.remove();
            }
        }
    }

}
