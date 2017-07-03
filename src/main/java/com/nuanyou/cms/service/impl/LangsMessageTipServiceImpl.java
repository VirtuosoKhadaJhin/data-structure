package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.dao.CmsUserDao;
import com.nuanyou.cms.dao.EntityNyLangsMessageTipDao;
import com.nuanyou.cms.entity.CmsUser;
import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import com.nuanyou.cms.model.LangsMessageTipVo;
import com.nuanyou.cms.service.LangsMessageTipService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

/**
 * Created by 孙昊 on 2017/6/13.
 */
@Service
public class LangsMessageTipServiceImpl implements LangsMessageTipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LangsMessageTipServiceImpl.class);

    @Autowired
    @Qualifier("s3")
    private FileClient fileClient;

    @Autowired
    private EntityNyLangsMessageTipDao messageTipDao;

    @Autowired
    private CmsUserDao cmsUserDao;

    @Override
    public EntityNyLangsMessageTip add(LangsMessageTipVo requestVo) {
        String imgUrl = requestVo.getImgUrl();
        EntityNyLangsMessageTip entityResult = messageTipDao.findByKeyCode(requestVo.getKeyCode());
        if (StringUtils.isEmpty(imgUrl)) {
            if (null != entityResult) {
                imgUrl = entityResult.getImgUrl();
            }
        }

        String email = UserHolder.getUser().getEmail();
        CmsUser cmsUser = cmsUserDao.findByEmail(email);

        EntityNyLangsMessageTip entityNyLangsMessageTip = new EntityNyLangsMessageTip(requestVo.getNewKeyCode(), requestVo.getRemark(), imgUrl);
        entityNyLangsMessageTip.setKeyCode(requestVo.getKeyCode());
        if(null != entityResult){
            entityNyLangsMessageTip.setId(entityResult.getId());
        }
        entityNyLangsMessageTip.setCreateDt(new Date());
        entityNyLangsMessageTip.setUserId(cmsUser.getId());
        return messageTipDao.save(entityNyLangsMessageTip);
    }

    @Override
    public String uploadImg(MultipartFile file) {
        String fileType = "";
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains("."))
                fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            InputStream is = file.getInputStream();
            String imgUrl = fileClient.uploadFile(is, fileType);

            // imgUrl = imgUrl.replace("https://", "http://dev.");
            // https://kr.file.91nuanyou.com/14975265854394393273.png
            return imgUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LangsMessageTipVo viewLangsMessageTip(LangsMessageTipVo requestVo, String keyCode) {
        EntityNyLangsMessageTip entityResult = messageTipDao.findByKeyCode(keyCode);

        if (null != entityResult) {
            LangsMessageTipVo langsMessageTipVo = new LangsMessageTipVo(entityResult.getRemark(),
                    entityResult.getImgUrl());
            return langsMessageTipVo;
        }

        return null;
    }

}
