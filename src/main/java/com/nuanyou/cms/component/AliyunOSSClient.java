package com.nuanyou.cms.component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.nuanyou.cms.util.ContentTypeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component("oss")
public class AliyunOSSClient extends FileClient {

    private OSSClient client;

    private String bucketName;

    private String domain;

    public AliyunOSSClient(@Value("${oss.bucketName}") String bucketName,
                           @Value("${oss.domain}") String domain,
                           @Value("${oss.endpoint}") String endpoint,
                           @Value("${oss.accessKeyId}") String accessKeyId,
                           @Value("${oss.secretAccessKey}") String secretAccessKey) {
        this.bucketName = bucketName;
        this.domain = domain;
        this.client = new OSSClient(endpoint, accessKeyId, secretAccessKey);
    }

    public String uploadFile(String filePath, InputStream is) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        metadata.setContentType(ContentTypeUtils.getContentType(filePath));
        client.putObject(bucketName, filePath, is, metadata);
        return new StringBuilder(domain).append("/").append(filePath).toString();
    }

}