package com.nuanyou.cms.service;

import com.nuanyou.cms.model.mission.*;
import org.springframework.data.domain.Page;

/**
 * Created by Byron on 2017/6/27.
 */
public interface MissionTaskService {

    /**
     * 查询所有的战队任务
     *
     * @param requestVo
     * @return
     */
    Page<MissionTaskVo> findAllMissionTask(MissionRequestVo requestVo);

    /**
     * 审核BD任务
     *
     * @param vo
     */
    void approvalTask(MissionRequestVo vo);

    /**
     * 指派任务到BD
     *
     * @param vo
     */
    void distributeTask(MissionDistributeParamVo vo);

    /**
     * 签到图历史
     *
     * @param requestVo
     * @return
     */
    Page<MissionBdMerchantTrack> findAllTrackByMchId(MissionBdMerchantTrack requestVo);

    /**
     * 新建任務
     *
     * @param vo
     */
    void createTask(MissionCreateParamVo vo);

}
