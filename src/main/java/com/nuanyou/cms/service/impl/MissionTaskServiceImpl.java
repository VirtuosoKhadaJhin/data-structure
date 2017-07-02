package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.MissionMerchatTrackDao;
import com.nuanyou.cms.dao.MissionTaskDao;
import com.nuanyou.cms.entity.enums.MissionTaskStatus;
import com.nuanyou.cms.entity.mission.BdMerchantTrack;
import com.nuanyou.cms.entity.mission.MissionTask;
import com.nuanyou.cms.model.MissionDistributeParamVo;
import com.nuanyou.cms.model.MissionRequestVo;
import com.nuanyou.cms.model.MissionTaskVo;
import com.nuanyou.cms.service.MissionTaskService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
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

    @Override
    public Page<MissionTaskVo> findAllMissionTask(final MissionRequestVo requestVo) {
        Specification spec = new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (requestVo.getMchId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("id"), requestVo.getMchId()));
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
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(requestVo.getFinishDt());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("finishDt").as(Date.class), dayStartEndTime.getLeft()));
                    predicate.add(cb.lessThanOrEqualTo(root.get("finishDt").as(Date.class), dayStartEndTime.getRight()));
                }
                if (requestVo.getAuditDt() != null) {
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(requestVo.getAuditDt());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("auditDt").as(Date.class), dayStartEndTime.getLeft()));
                    predicate.add(cb.lessThanOrEqualTo(root.get("auditDt").as(Date.class), dayStartEndTime.getRight()));
                }
                predicate.add(cb.equal(root.get("delFlag").as(Boolean.class), false));
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        long totalNum = missionTaskDao.count(spec);
        List<MissionTask> missionTasks = missionTaskDao.findAll(spec);
        List<MissionTaskVo> taskVos = this.setMerchantTrackValue(missionTasks);
        Pageable pageable = new PageRequest(requestVo.getPageNum() - 1, requestVo.getPageSize());
        Page<MissionTaskVo> page = new PageImpl<MissionTaskVo>(taskVos, pageable, totalNum);
        return page;
    }

    @Override
    public void approvalTask(MissionRequestVo vo) {
        if (vo.getStatus() == null || vo.getMchId() == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        Long userid = UserHolder.getUser().getUserid();
        missionTaskDao.updateTaskStatus(vo.getMchId(), vo.getStatus().getKey(), vo.getRemark(), userid, new Date());
    }

    @Override
    public void distributeTask(MissionDistributeParamVo vo) {
        if (vo.getBdId() == null || vo.getTaskIds() == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        missionTaskDao.distributeTask(vo.getBdId(), MissionTaskStatus.UN_FINISH.getKey(), vo.getDistrDt(), new Date(), vo.getTaskIds());
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
        Iterator<BdMerchantTrack> iterator = tracks.iterator();
        while (iterator.hasNext()) {
            BdMerchantTrack next = iterator.next();
            Long mchId = next.getMchId();
            Long userId = next.getUserId();
            if (!maps.containsKey(mchId)) {
                continue;
            }
            MissionTaskVo missionTaskVo = maps.get(mchId);
            if (userId.equals(missionTaskVo.getBdUser().getId())) {//相同BD的时候才需要添加(存在多个BD录入商户信息)
                missionTaskVo.setMerchantTrack(next);
                iterator.remove();
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
}
