package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.LangsMessageTipVo;
import com.nuanyou.cms.service.LangsMessageTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 孙昊 on 2017/6/13.
 */
@Controller
@RequestMapping("langsMessageTip")
public class LangsMessageTipController {

    @Autowired
    private LangsMessageTipService messageTipService;

    /**
     * 添加备注语言
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public APIResult<LangsDictionary> remove(@RequestBody LangsMessageTipVo requestVo) {
        APIResult result = new APIResult(ResultCodes.Success);
        messageTipService.add(requestVo);
        return result;
    }


}
