package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.MissionRequestVo;
import com.nuanyou.cms.model.MissionTaskVo;
import com.nuanyou.cms.service.MissionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Byron on 2017/6/27.
 */
@Controller
@RequestMapping("mission_task")
public class MissionTaskController {

    @Autowired
    private MissionTaskService missionTaskService;

    /**
     * 审核战队任务列表
     *
     * @param requestVo
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String findAllMissionTask(MissionRequestVo requestVo, Model model) {
        Page<MissionTaskVo> page = missionTaskService.findAllMissionTask(requestVo);
        model.addAttribute("page", page);
        model.addAttribute("requestVo", requestVo);
        return "misssion/list";
    }

    @RequestMapping("approval")
    @ResponseBody
    public APIResult approvalTask(@RequestBody MissionRequestVo vo) {
        missionTaskService.approvalTask(vo);
        return new APIResult(ResultCodes.Success);
    }
}
