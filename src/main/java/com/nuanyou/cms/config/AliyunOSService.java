package com.nuanyou.cms.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AliyunOSService {

    private OSSClient client;


    public AliyunOSService setOSSClient(String endpoint, String accessKeyId, String secretAccessKey) {
        this.client = new OSSClient(endpoint, accessKeyId, secretAccessKey);
        return this;
    }


    public void createFolder(String bucketName, String folderName) throws IOException {
        ObjectMetadata objectMeta = new ObjectMetadata();
        /*
         * 这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,dataStream仍然可以有数据
		 * 照样可以上传下载,只是控制台会对以"/"结尾的Object以文件夹的方式展示,用户可以利用这种方式来实现文件夹模拟功能,创建形式上的文件夹
		 */
        if (!folderName.endsWith("/")) {
            folderName = folderName + "/";
        }
        byte[] buffer = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        objectMeta.setContentLength(0);
        try {
            client.putObject(bucketName, folderName, in, objectMeta);
        } finally {
            in.close();
        }
    }

    public void uploadFile(String bucketName, String bucketFilePath, File file) throws IOException {
        client.putObject(bucketName, bucketFilePath, file);
    }

    public void uploadFile(String bucketName, String bucketFilePath, InputStream is, ObjectMetadata metadata) {
        client.putObject(bucketName, bucketFilePath, is, metadata);
    }
}