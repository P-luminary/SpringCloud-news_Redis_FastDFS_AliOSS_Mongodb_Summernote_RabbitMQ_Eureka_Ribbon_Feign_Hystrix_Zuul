package com.imooc.files.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploaderService {
    /**
     * 使用fastdfs 上传文件
     */
    public String uploadFdfs(MultipartFile file, String fileExtName) throws IOException;

    /**
     * 使用OSS 上传文件
     */
    public String uploadOSS(MultipartFile file,String userId, String fileExtName) throws IOException;
}
