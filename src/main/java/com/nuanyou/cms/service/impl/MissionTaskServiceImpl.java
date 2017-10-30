package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.BdUser;
import com.nuanyou.cms.entity.CmsUser;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.enums.MerchantCooperationStatus;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import com.nuanyou.cms.entity.mission.BdMerchantTrack;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.entity.mission.MissionTask;
import com.nuanyou.cms.model.mission.*;
import com.nuanyou.cms.service.MissionTaskService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
 * Created by Byron on 2017/6/27.
 */
@Service
public class MissionTaskServiceImpl implements MissionTaskService {

    @Autowired
    private MissionTaskDao missionTaskDao;

    @Autowired
    private MissionMerchatTrackDao trackDao;

    @Autowired
    private CmsUserDao userDao;

    @Autowired
    private BdUserDao bdUserDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public Page<MissionTaskVo> findAllMissionTask(final MissionRequestVo requestVo) {
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageSize());
        final List<Long> countryIds = userService.findUserCountryId();
        Specification spec = new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                }
                if (requestVo.getMchId() != null) {
                    requestVo.getMchIds().add(requestVo.getMchId());
                }
                if (CollectionUtils.isNotEmpty(requestVo.getMchIds())) {
                    predicate.add(root.get("merchant").get("id").in(requestVo.getMchIds()));
                }
                if (requestVo.getCooperationStatus() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("cooperationStatus").as(MerchantCooperationStatus.class), requestVo.getCooperationStatus()));
                }
                if (requestVo.getStatus() == null) {
                    if (requestVo.getAudit()) {//审核列表
                        ArrayList<MissionTaskStatus> auditStatus = Lists.newArrayList(MissionTaskStatus.FINISHED, MissionTaskStatus.APPROVED, MissionTaskStatus.NON_APPROVAL);
                        predicate.add(root.get("status").in(auditStatus));
                    }
                } else {
                    predicate.add(cb.equal(root.get("status"), requestVo.getStatus().getKey()));
                }
                if (requestVo.getDistrict() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("id"), requestVo.getDistrict()));
                }
                if (StringUtils.isNotEmpty(requestVo.getVersion())) {
                    predicate.add(cb.equal(root.get("version"), requestVo.getVersion()));
                }
                if (requestVo.getBdId() != null) {
                    predicate.add(cb.equal(root.get("bdUser").get("id"), requestVo.getBdId()));
                }
                if (requestVo.getGroupId() != null) {
                    predicate.add(cb.equal(root.get("group").get("id"), requestVo.getGroupId()));
                }
                if (requestVo.getCountry() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("country").get("id"), requestVo.getCountry()));
                }
                if (requestVo.getCity() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("city").get("id"), requestVo.getCity()));
                }
                if (requestVo.getFinishDt() != null) {
                    //edit finishDt to by young at 2017-09-12
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(requestVo.getFinishDt());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("visitDt").as(Date.class), dayStartEndTime.getLeft()));
                    predicate.add(cb.lessThanOrEqualTo(root.get("visitDt").as(Date.class), dayStartEndTime.getRight()));
                }
                if (requestVo.getAuditDt() != null) {
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(requestVo.getAuditDt());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("auditDt").as(Date.class), dayStartEndTime.getLeft()));
                    predicate.add(cb.lessThanOrEqualTo(root.get("auditDt").as(Date.class), dayStartEndTime.getRight()));
                }
                if (requestVo.getDistrDt() != null) {
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(requestVo.getDistrDt());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("distrDt").as(Date.class), dayStartEndTime.getLeft()));
                    predicate.add(cb.lessThanOrEqualTo(root.get("distrDt").as(Date.class), dayStartEndTime.getRight()));
                }
                predicate.add(cb.equal(root.get("delFlag").as(Boolean.class), false));
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("updateDt")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        };
        Page<MissionTask> missionTasks = missionTaskDao.findAll(spec, pageable);
        List<MissionTaskVo> taskVos = this.setMerchantTrackValue(missionTasks.getContent());
        Page<MissionTaskVo> page = new PageImpl<MissionTaskVo>(taskVos, pageable, missionTasks.getTotalElements());
        return page;
    }

    @Override
    public void approvalTask(MissionRequestVo vo) {
        if (vo.getStatus() == null || vo.getMchId() == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        String dmail = UserHolder.getUser().getEmail();
        CmsUser cmsUser = userDao.findByEmail(dmail);
        if (vo.getStatus().equals(MissionTaskStatus.APPROVED)){
            missionTaskDao.approveThroughTask(vo.getMchId(), vo.getStatus().getKey(), vo.getRemark(), cmsUser.getId(), new Date());
        }else {
            missionTaskDao.updateTaskStatus(vo.getMchId(), vo.getStatus().getKey(), vo.getRemark(), cmsUser.getId(), new Date());
        }

    }

    @Override
    public void distributeTask(MissionDistributeParamVo vo) {
        if (vo.getBdId() == null || vo.getTaskIds() == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        missionTaskDao.distributeTask(vo.getBdId(), MissionTaskStatus.UN_FINISH.getKey(), vo.getDistrDt(), new Date(), vo.getTaskIds());
    }

    @Override
    public void changeType(TaskTypeChangeVo vo){
        if (vo.getTaskType() == null || vo.getTaskIds() == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        missionTaskDao.changeType(new Date(),vo.getTaskIds(),vo.getTaskType());
    }

    @Override
    public Page<MissionBdMerchantTrack> findAllTrackByMchId(final MissionBdMerchantTrack requestVo) {
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageSize());
        if (requestVo.getMchId() == null) {
            return new PageImpl(Lists.newArrayList(), pageable, 0);
        }
        Specification spec = new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (requestVo.getMchId() != null) {
                    predicate.add(cb.equal(root.get("mchId"), requestVo.getMchId()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("updateTime")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        };
        Page<BdMerchantTrack> bdMerchantTracks = trackDao.findAll(spec, pageable);
        List<BdMerchantTrack> content = bdMerchantTracks.getContent();
        List<MissionBdMerchantTrack> tracks = this.convertToMissionTracks(content);
        final Collection<Long> userIds = CollectionUtils.collect(content, new Transformer() {
            @Override
            public Long transform(Object input) {
                return ((BdMerchantTrack) input).getUserId();
            }
        });

        if (CollectionUtils.isNotEmpty(userIds)) {
            Map<Long, String> users = new HashMap();
            List<BdUser> trackUsers = bdUserDao.findByIds(Lists.newArrayList(userIds));
            for (BdUser bdUser : trackUsers) {
                users.put(bdUser.getId(), bdUser.getChineseName());
            }
            for (MissionBdMerchantTrack track : tracks) {
                String name = users.get(track.getUserId());
                if(StringUtils.isNotEmpty(name)){
                    track.setUsername(name);
                }
            }
        }

        Page<MissionBdMerchantTrack> merchantTracks = new PageImpl(tracks, pageable, bdMerchantTracks.getTotalElements());
        return merchantTracks;
    }

    /**
     * 设置merchant track
     *
     * @param missionTasks
     * @return
     */
    private List<MissionTaskVo> setMerchantTrackValue(List<MissionTask> missionTasks) {
        final Collection<Long> ids = CollectionUtils.collect(missionTasks, new Transformer() {
            @Override
            public Long transform(Object input) {
                return ((MissionTask) input).getMerchant().getId();
            }
        });

        Map<Long, MissionTaskVo> maps = new LinkedHashMap<Long, MissionTaskVo>(missionTasks.size());
        for (MissionTask missionTask : missionTasks) {
            MissionTaskVo taskVo = BeanUtils.copyBeanNotNull(missionTask, new MissionTaskVo());
            taskVo.setStatus(MissionTaskStatus.toEnum(missionTask.getStatus()));
            maps.put(missionTask.getMerchant().getId(), taskVo);
        }
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList(maps.values());
        }
        List<BdMerchantTrack> tracks = trackDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(root.get("mchId").in(ids));
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("id")));
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });
        Map<Long,String> map = new HashMap<>();
        for (BdMerchantTrack bdMerchantTrack : tracks) {
            if (!map.containsKey(bdMerchantTrack.getMchId()))
                map.put(bdMerchantTrack.getMchId(),bdMerchantTrack.getVisitType()!=null?bdMerchantTrack.getVisitType().getName():null);
        }
        for (Map.Entry entry : maps.entrySet())
            maps.get(entry.getKey()).setVisitType(map.get(entry.getKey()));
        Iterator<BdMerchantTrack> iterator = tracks.iterator();
        while (iterator.hasNext()) {
            BdMerchantTrack next = iterator.next();
            Long mchId = next.getMchId();
            Long userId = next.getUserId();
            if (!maps.containsKey(mchId)) {
                continue;
            }
            MissionTaskVo missionTaskVo = maps.get(mchId);
            if (missionTaskVo.getStatus() != MissionTaskStatus.UN_DISTRIBUTE) {
                if (userId.equals(missionTaskVo.getBdUser().getId())) {//相同BD的时候才需要添加(存在多个BD录入商户信息)
                    missionTaskVo.setMerchantTrack(next);
                    iterator.remove();
                }
            }
        }
        return Lists.newArrayList(maps.values());
    }

    public List<MissionTaskVo> covertToMissionTaskVos(List<MissionTask> missionTasks) {
        if (CollectionUtils.isEmpty(missionTasks)) {
            return Lists.newArrayList();
        }
        List<MissionTaskVo> taskVos = Lists.newArrayList();
        for (MissionTask task : missionTasks) {
            MissionTaskVo vo = BeanUtils.copyBeanNotNull(task, new MissionTaskVo());
            vo.setStatus(MissionTaskStatus.toEnum(task.getStatus()));
            taskVos.add(vo);
        }
        return taskVos;
    }

    public List<MissionBdMerchantTrack> convertToMissionTracks(List<BdMerchantTrack> bdMerchantTracks) {
        if (CollectionUtils.isEmpty(bdMerchantTracks)) {
            return Lists.newArrayList();
        }
        List<MissionBdMerchantTrack> tracks = Lists.newArrayList();
        for (BdMerchantTrack track : bdMerchantTracks) {
            MissionBdMerchantTrack vo = BeanUtils.copyBeanNotNull(track, new MissionBdMerchantTrack());
            tracks.add(vo);
        }
        return tracks;

    }

    @Override
    public void createTask(MissionCreateParamVo vo) {
        Merchant merchant = merchantDao.findOne(vo.getMchId());
        if (merchant == null)
            throw new APIException(ResultCodes.NotFoundMerchant);
        List<MissionTask> tasks = missionTaskDao.findByMerchant(merchant);
        if (tasks != null && tasks.size() > 0)
            throw new APIException(ResultCodes.MissionExists);
        MissionTask task = new MissionTask();
        task.setMerchant(merchant);
        task.setVersion(vo.getVersion());
        task.setDelFlag(false);
        task.setMchName(merchant.getName());
        task.setStatus(0);
        MissionGroup group = new MissionGroup();
        group.setId(vo.getGroupId());
        task.setGroup(group);
        task.setCreateDt(new Date());
        task.setUpdateDt(new Date());
        missionTaskDao.save(task);
    }
}
