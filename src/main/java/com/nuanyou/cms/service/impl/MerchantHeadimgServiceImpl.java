package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.config.ImageSpec;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.dao.MerchantHeadimgDao;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantHeadimg;
import com.nuanyou.cms.model.MerchantVO;
import com.nuanyou.cms.service.FileUploadService;
import com.nuanyou.cms.service.MerchantHeadimgService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
@Service
public class MerchantHeadimgServiceImpl implements MerchantHeadimgService {

    @Autowired
    private MerchantHeadimgDao merchantHeadimgDao;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public MerchantHeadimg saveNotNull(MerchantHeadimg entity) {
        if (entity.getId() == null) {
            Integer sort = entity.getSort();
            if (sort == null)
                entity.setSort(999);
            entity = merchantHeadimgDao.save(entity);
            if (sort != null && sort == 0)
                setTop(entity.getId());
            return entity;
        }
        MerchantHeadimg oldEntity = merchantHeadimgDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantHeadimgDao.save(oldEntity);
    }

    @Override
    public List<MerchantHeadimg> find(MerchantHeadimg entity) {
        return merchantHeadimgDao.findAll(Example.of(entity), new Sort("sort"));
    }

    @Override
    public void setTop(Long id) {
        MerchantHeadimg headimg = merchantHeadimgDao.findOne(id);
        if (headimg == null)
            return;
        List<MerchantHeadimg> merchantHeadimgs = find(new MerchantHeadimg(headimg.getMchId()));
        int sort = 1;
        for (MerchantHeadimg merchantHeadimg : merchantHeadimgs) {
            if (merchantHeadimg.getId().equals(id)) {
                merchantHeadimg.setSort(0);
            } else {
                merchantHeadimg.setSort(sort++);
            }
        }
        merchantHeadimgDao.save(merchantHeadimgs);
    }

    public APIResult setListImgUrl(Long id, String detailImgUrl, ImageSpec imageSpec) throws Exception{
        APIResult result = new APIResult();
        String fileType = "";
        String callBackImgUrl = "";
        try {
            URL url =new URL(detailImgUrl);
            URLConnection imgconn = url.openConnection();
            ImageUtils.File imgFile = ImageUtils.process(imgconn.getInputStream(), imageSpec);
            fileType = imgFile.getFileType();
            InputStream is = new ByteArrayInputStream(imgFile.getData());
            callBackImgUrl = fileUploadService.commonUpdateImg(is, fileType);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Merchant entity = merchantDao.findOne(id);
        entity.setListImgUrl(callBackImgUrl);

        if (entity.getFirstshowTime() == null && Boolean.TRUE.equals(entity.getDisplay())) {
            entity.setFirstshowTime(new Date());
        }
        entity = merchantDao.save(entity);
        result.setMsg("列表图设置成功！");
        result.setCode(0);
        return result;

    }

}




