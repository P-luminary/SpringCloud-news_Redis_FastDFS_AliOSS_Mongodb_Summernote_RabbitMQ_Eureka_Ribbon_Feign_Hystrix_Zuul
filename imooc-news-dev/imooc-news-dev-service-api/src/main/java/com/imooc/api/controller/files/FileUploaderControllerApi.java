package com.imooc.api.controller.files;


import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "文件上传的controller",tags = {"xx功能的Controller"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {
    /**
     * 上传单文件
     * @param userId
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;

    /**
     * 上传多文件
     * @param userId
     * @param files
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("/uploadSomeFiles")  //因为前端createArticle.html 178行 multiForm.append('files',f,f.name);
    public GraceJSONResult uploadSomeFiles(@RequestParam String userId, MultipartFile[] files) throws Exception;

    //不可以通过swagger2调用的

    /**
     * 文件上传到mongodb的gridfs中
     * @param newAdminBO
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadToGridFS")
    public GraceJSONResult uploadToGridFS(@RequestBody NewAdminBO newAdminBO) throws Exception;

    @GetMapping("/readInGridFS")
    public void readInGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 从gridfs中读取图片内容 返回base64数据
     * @param faceId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/readFace64InGridFS")
    public GraceJSONResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception;
}

