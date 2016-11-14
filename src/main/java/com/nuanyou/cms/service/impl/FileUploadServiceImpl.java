package com.nuanyou.cms.service.impl;

import com.aliyun.oss.model.ObjectMetadata;
import com.nuanyou.cms.config.AliyunOSService;
import com.nuanyou.cms.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 2016/9/22.
 */
@Service("fileUploadService")
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${aliyun.nuanyou.image-server}")
    private String nuanyouServer;
    @Value("${aliyun.nuanyou.bucketname}")
    private String nuanyouBucketName;
    @Value("${aliyun.nuanyou.cms-basepath}")
    private String nuanyouBasePath;
    @Value("${aliyun.nuanyou.fakeuser-basepath}")
    private String nuanyouFakeUserBasePath;
    @Value("${aliyun.nuanyou.fakercomment-basepath}")
    private String nuanyouFakeCommentBasePath;

    @Autowired
    private AliyunOSService aliyunOSService;

    private static final SimpleDateFormat dirSdf = new SimpleDateFormat("yyyy/MM/dd");

    private static final SimpleDateFormat fileSdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public String commonUpdateImg(InputStream is, String fileType) throws Exception {
        Date now = new Date();

        String dirPath = dirSdf.format(now);
        String fileName = fileSdf.format(now) + (int) (Math.random() * 9000 + 1000);
        String filePath = nuanyouBasePath + "/" + dirPath + "/" + fileName + "." + fileType;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        aliyunOSService.uploadFile(nuanyouBucketName, filePath, new BufferedInputStream(is), metadata);
        String callBackImgUrl = nuanyouServer + "/" + filePath;
        return callBackImgUrl;
    }

}