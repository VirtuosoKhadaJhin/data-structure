package com.nuanyou.cms.component;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.nuanyou.cms.util.ContentTypeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component("s3")
public class AwsS3Client extends FileClient {

    private AmazonS3Client client;

    @Value("${s3.bucketName}")
    private String bucketName;

    @Value("${s3.domain}")
    private String domain;

    @Value("${s3.region}")
    private String region;

    public AwsS3Client(@Value("${s3.bucketName}") String bucketName,
                       @Value("${s3.domain}") String domain,
                       @Value("${s3.region}") String region,
                       @Value("${s3.accessKey}") String accessKey,
                        @Value("${s3.secretKey}") String secretKey) {
        this.bucketName = bucketName;
        this.domain = domain;
        this.region = region;
        if ("dev.img.91nuanyou.com".equals(bucketName)) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            this.client = new AmazonS3Client(credentials);
        } else {
            this.client = new AmazonS3Client();
        }

        client.setRegion(Region.getRegion(Regions.fromName(this.region)));
    }


    public String uploadFile(String filePath, InputStream is) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        metadata.setContentType(ContentTypeUtils.getContentType(filePath));
        client.putObject(bucketName, filePath, is, metadata);

        AccessControlList acl = client.getObjectAcl(bucketName, filePath);

        acl.revokeAllPermissions(GroupGrantee.AllUsers);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        SetObjectAclRequest request = new SetObjectAclRequest(bucketName, filePath, acl);
        client.setObjectAcl(request);
        return new StringBuilder(domain).append("/").append(filePath).toString();
    }

    public InputStream queryFile(String filePath) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, filePath);
        S3Object object = client.getObject(getObjectRequest);
        InputStream objectData = object.getObjectContent();
        return objectData;
    }



    /**
     * 获取JSON格式配置文件
     *
     * @param key
     * @return
     * @throws IOException
     */
    public JSONObject acquireConfig(String key) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        S3Object object = client.getObject(getObjectRequest);
        InputStream objectData = object.getObjectContent();
        return displayTextInputStream(objectData);
    }

    private static JSONObject displayTextInputStream(InputStream input) throws IOException {
        StringBuilder json = new StringBuilder();
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(input));
        String inputLine = null;
        while ((inputLine = reader.readLine()) != null) {
            json.append(inputLine);
        }
        JSONObject response = JSONObject.parseObject(json.toString());
        reader.close();
        return response;
    }

}