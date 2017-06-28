package com.nuanyou.cms.service;

import com.nuanyou.cms.model.MissionRequestVo;
import com.nuanyou.cms.model.MissionTaskVo;
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
}
