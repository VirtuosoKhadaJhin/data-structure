package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import com.nuanyou.cms.model.LangsMessageTipVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 孙昊 on 2017/6/13.
 */
public interface LangsMessageTipService {

    /**
     * 添加备注
     *
     * @param requestVo
     * @return
     */
    EntityNyLangsMessageTip add(LangsMessageTipVo requestVo);

    /**
     * 上传图片
     *
     * @return
     */
    String uploadImg(MultipartFile file);

    /**
     * 查看keyCode的备注信息
     *
     * @param requestVo
     * @param keyCode
     * @return
     */
    LangsMessageTipVo viewLangsMessageTip(LangsMessageTipVo requestVo, String keyCode);

}
