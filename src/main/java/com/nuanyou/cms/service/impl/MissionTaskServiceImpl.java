package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.MissionTaskDao;
import com.nuanyou.cms.entity.mission.MissionTask;
import com.nuanyou.cms.model.MissionRequestVo;
import com.nuanyou.cms.model.MissionTaskVo;
import com.nuanyou.cms.service.MissionTaskService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Byron on 2017/6/27.
 */
@Service
public class MissionTaskServiceImpl implements MissionTaskService {

    @Autowired
    private MissionTaskDao missionTaskDao;

    @Override
    public Page<MissionTaskVo> findAllMissionTask(final MissionRequestVo requestVo) {
        Specification spec = new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (requestVo.getMchId() != null) {
                    predicate.add(cb.equal(root.get("mchId"), requestVo.getMchId()));
                }
                predicate.add(cb.equal(root.get("status"), requestVo.getStatus().getKey()));
                predicate.add(cb.equal(root.get("delFlag").as(Boolean.class), false));
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.asc(root.get("updateDt")));
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        long totalNum = missionTaskDao.count(spec);
        List<MissionTask> missionTasks = missionTaskDao.findAll(spec);

        List<MissionTaskVo> missionTaskVos = this.covertToMissionTaskVos(missionTasks);

        Pageable pageable = new PageRequest(requestVo.getPageNum() - 1, requestVo.getPageSize());
        Page<MissionTaskVo> page = new PageImpl<MissionTaskVo>(missionTaskVos, pageable, totalNum);
        return page;
    }

    @Override
    public void approvalTask(MissionRequestVo vo) {
        if (vo.getStatus() == null || vo.getMchId() == null) {
            throw new APIException(ResultCodes.MissingParameter, ResultCodes.MissingParameter.getMessage());
        }
        missionTaskDao.updateTaskStatus(vo.getMchId(), vo.getStatus().getKey(), vo.getRemark());
    }

    private List<MissionTaskVo> covertToMissionTaskVos(List<MissionTask> missionTasks) {
        if (CollectionUtils.isEmpty(missionTasks)) {
            return Lists.newArrayList();
        }
        List<MissionTaskVo> taskVos = Lists.newArrayList();
        for (MissionTask task : missionTasks) {
            taskVos.add(BeanUtils.copyBeanNotNull(task, new MissionTaskVo()));
        }
        return taskVos;
    }
}
