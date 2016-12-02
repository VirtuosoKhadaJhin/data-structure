package com.nuanyou.cms.service.impl;

import com.aliyun.oss.model.ObjectMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.nuanyou.cms.config.AliyunOSService;
import com.nuanyou.cms.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @Value("${upload.s3server}")
    private String s3Server;

    private static String region = "ap-northeast-2";
    private static String bucket_name = "nuanyou-file-prod-ap-northeast-2";

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
//        aliyunOSService.uploadFile(nuanyouBucketName, filePath, new BufferedInputStream(is), metadata);
//        String callBackImgUrl = nuanyouServer + "/" + filePath;

        this.unloadFileByS3(new BufferedInputStream(is), filePath);
        String callBackImgUrl = s3Server + "/" + filePath;
        return callBackImgUrl;
    }


    private void unloadFileByS3(BufferedInputStream b, String filePath) {
        com.amazonaws.services.s3.model.ObjectMetadata met = new com.amazonaws.services.s3.model.ObjectMetadata();

        AmazonS3Client ec2Client = new AmazonS3Client();
        Region apNorthEast2 = Region.getRegion(Regions.fromName(region));
        ec2Client.setRegion(apNorthEast2);
        PutObjectResult result = ec2Client.putObject(bucket_name, filePath, b, met);

        AccessControlList acl = ec2Client.getObjectAcl(bucket_name, filePath);
        List<Grant> list = acl.getGrantsAsList();

        acl.revokeAllPermissions(GroupGrantee.AllUsers);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        SetObjectAclRequest request = new SetObjectAclRequest(bucket_name, filePath, acl);
        ec2Client.setObjectAcl(request);
    }

}