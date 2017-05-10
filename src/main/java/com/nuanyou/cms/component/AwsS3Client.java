package com.nuanyou.cms.component;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component("s3")
public class AwsS3Client extends FileClient {

    private AmazonS3Client client;

    private String bucketName;

    private String domain;

    private String region;

    public AwsS3Client(@Value("${s3.bucketName}") String bucketName,
                       @Value("${s3.domain}") String domain,
                       @Value("${s3.region}") String region) {
        this.bucketName = bucketName;
        this.domain = domain;
        this.region = region;
        this.client = new AmazonS3Client();
        client.setRegion(Region.getRegion(Regions.fromName(this.region)));
    }

    @Override
    public String uploadFile(String filePath, File file) {
        client.putObject(bucketName, filePath, file);

        AccessControlList acl = client.getObjectAcl(bucketName, filePath);

        acl.revokeAllPermissions(GroupGrantee.AllUsers);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        SetObjectAclRequest request = new SetObjectAclRequest(bucketName, filePath, acl);
        client.setObjectAcl(request);
        return new StringBuilder(domain).append("/").append(filePath).toString();
    }

    @Override
    public String uploadFile(String filePath, InputStream is) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        client.putObject(bucketName, filePath, is, metadata);

        AccessControlList acl = client.getObjectAcl(bucketName, filePath);

        acl.revokeAllPermissions(GroupGrantee.AllUsers);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        SetObjectAclRequest request = new SetObjectAclRequest(bucketName, filePath, acl);
        client.setObjectAcl(request);
        return new StringBuilder(domain).append("/").append(filePath).toString();
    }

}