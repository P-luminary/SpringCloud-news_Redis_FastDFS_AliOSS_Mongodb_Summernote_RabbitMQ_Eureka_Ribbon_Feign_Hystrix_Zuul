package com.imooc.files.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:file-${spring.profiles.active}.properties ") //这个是在application.yml里面的 自动匹配
@ConfigurationProperties(prefix = "file")
public class FileResource {
    private String host;
    private String endpoint;
    private String BucketName;
    private String objectName;
    private String OssHost;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return BucketName;
    }

    public void setBucketName(String bucketName) {
        BucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOssHost() {
        return OssHost;
    }

    public void setOssHost(String ossHost) {
        OssHost = ossHost;
    }
}
