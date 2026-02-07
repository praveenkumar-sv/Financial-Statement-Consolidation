package com.example.aggregator.services;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class AWSService {

    private final S3Client s3Client;
    private final String bucketName;
    @Value("${aws.s3.prefix.transactions}")
    private String transactionsPrefix;
    public AWSService(@Value("${aws.accessKeyId}") String accessKeyId,
                      @Value("${aws.secretAccessKey}") String secretAccessKey,
                      @Value("${aws.s3.bucket}") String bucketName) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }

    public String uploadFile(String filePath, String fileName) {
        String key=transactionsPrefix+fileName;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, Paths.get(filePath));
        return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileName)).toExternalForm();
    }

    public void downloadFileToLocal(String fileName) throws IOException {
        String key = transactionsPrefix + fileName; // full path in S3
        String localFilePath = System.getProperty("user.home") + "/Downloads/" + fileName;
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        FileUtils.copyInputStreamToFile(s3Object, new File(localFilePath));
    }

}
