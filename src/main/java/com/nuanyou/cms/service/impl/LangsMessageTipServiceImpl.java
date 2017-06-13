package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.dao.EntityNyLangsMessageTipDao;
import com.nuanyou.cms.entity.EntityNyLangsMessageTip;
import com.nuanyou.cms.model.LangsMessageTipVo;
import com.nuanyou.cms.service.LangsMessageTipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

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
    public EntityNyLangsMessageTip add(LangsMessageTipVo requestVo) {
        String keyCode = requestVo.getKeyCode();
        try {
            keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        EntityNyLangsMessageTip entityNyLangsMessageTip = new EntityNyLangsMessageTip();
        entityNyLangsMessageTip.setKeyCode(requestVo.getKeyCode());

        Example<EntityNyLangsMessageTip> example = Example.of(entityNyLangsMessageTip);
        List<EntityNyLangsMessageTip> entityResult = messageTipDao.findAll(example);

        if (entityResult.size() > 0) {
            messageTipDao.delete(entityResult);
        }

        entityNyLangsMessageTip = new EntityNyLangsMessageTip(keyCode, requestVo.getRemark(),
                requestVo.getImgUrl(), new Date(), false);

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
            return imgUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LangsMessageTipVo viewLangsMessageTip(LangsMessageTipVo requestVo) {
        String keyCode = requestVo.getKeyCode();
        try {
            keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        EntityNyLangsMessageTip entityNyLangsMessageTip = new EntityNyLangsMessageTip();
        entityNyLangsMessageTip.setKeyCode(keyCode);

        Example<EntityNyLangsMessageTip> example = Example.of(entityNyLangsMessageTip);
        List<EntityNyLangsMessageTip> entityResult = messageTipDao.findAll(example);

        if (entityResult.size() > 0) {
            entityNyLangsMessageTip = entityResult.get(0);
            LangsMessageTipVo langsMessageTipVo = new LangsMessageTipVo(entityNyLangsMessageTip.getRemark(),
                    entityNyLangsMessageTip.getImgUrl());
            return langsMessageTipVo;
        }

        return null;
    }

}
