package com.nuanyou.cms.controller;

import com.nuanyou.cms.model.MissionDistributeRequestVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Byron on 2017/6/28.
 */
@Controller
@RequestMapping("mission_distribute")
public class MissionDistributeTaskController {

    @RequestMapping("list")
    public String list(MissionDistributeRequestVo vo, Model model) {
        return "mission-distribute/list";
    }
}
