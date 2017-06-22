package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.dao.EntityNyLangsMessageTipDao;
import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import com.nuanyou.cms.model.LangsMessageTipVo;
import com.nuanyou.cms.service.LangsMessageTipService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
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
    private EntityNyLangsDictionaryDao dictionaryDao;

    @Autowired
    private EntityNyLangsMessageTipDao messageTipDao;

    @Override
    public EntityNyLangsMessageTip add(LangsMessageTipVo requestVo, String keyCode) {
        String newKeyCode = requestVo.getNewKeyCode();
        String imgUrl = requestVo.getImgUrl();

        EntityNyLangsMessageTip entityNyLangsMessageTip = new EntityNyLangsMessageTip();
        entityNyLangsMessageTip.setKeyCode(keyCode);

        Example<EntityNyLangsMessageTip> example = Example.of(entityNyLangsMessageTip);
        EntityNyLangsMessageTip entityResult = messageTipDao.findByKeyCode(keyCode);

        if (imgUrl == null || imgUrl == "") {
            if (null != entityResult) {
                imgUrl = entityResult.getImgUrl();
            }
        }

        Long userid = UserHolder.getUser().getUserid();

        entityNyLangsMessageTip = new EntityNyLangsMessageTip(newKeyCode, requestVo.getRemark(), imgUrl, new Date(), false);
        if(null != entityResult){
            entityNyLangsMessageTip.setId(entityResult.getId());
        }

        entityNyLangsMessageTip.setUserId(userid);
        EntityNyLangsMessageTip result = messageTipDao.save(entityNyLangsMessageTip);
        return result;
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

            imgUrl = imgUrl.replace("https://", "http://dev.");
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
