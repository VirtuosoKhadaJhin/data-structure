package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import com.nuanyou.cms.model.LangsMessageTipVo;
import com.nuanyou.cms.service.LangsMessageTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

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
    public APIResult<EntityNyLangsMessageTip> remove(@RequestBody LangsMessageTipVo requestVo) throws UnsupportedEncodingException {
        APIResult<EntityNyLangsMessageTip> result = new APIResult<EntityNyLangsMessageTip>(ResultCodes.Success);
        String keyCode = requestVo.getKeyCode();
        keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        requestVo.setKeyCode(keyCode);
        EntityNyLangsMessageTip entityNyLangsMessageTip = messageTipService.add(requestVo);
        result.setData(entityNyLangsMessageTip);
        return result;
    }

    /**
     * 添加备注语言
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadImg")
    @ResponseBody
    public APIResult<String> uploadImg(MultipartFile file) {
        APIResult<String> result = new APIResult<String>(ResultCodes.Success);
        String imgUrl = messageTipService.uploadImg(file);
        result.setData(imgUrl);
        return result;
    }

    /**
     * 根据keyCode查询备注
     *
     * @param requestVo
     * @return
     */
    @RequestMapping("/viewLangsMessageTip")
    @ResponseBody
    public APIResult<LangsMessageTipVo> viewLangsMessageTip(@RequestBody LangsMessageTipVo requestVo) throws UnsupportedEncodingException {
        APIResult<LangsMessageTipVo> result = new APIResult<LangsMessageTipVo>(ResultCodes.Success);
        String keyCode = requestVo.getKeyCode();
        keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        LangsMessageTipVo langsMessageTipVo = messageTipService.viewLangsMessageTip(requestVo, keyCode);
        result.setData(langsMessageTipVo);
        return result;
    }

}
