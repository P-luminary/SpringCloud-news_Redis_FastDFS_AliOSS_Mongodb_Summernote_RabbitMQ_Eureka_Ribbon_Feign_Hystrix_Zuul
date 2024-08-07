package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegistLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid; //用户需求验证

@Api(value = "用户注册登录",tags = {"用户注册登录的Controller"})
@RequestMapping("passport")
public interface PassportControllerApi {
    @ApiOperation(value = "获得短信验证码",notes = "获得短信验证码",httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);

    @ApiOperation(value = "一键注册登录接口",notes = "一键注册登录接口",httpMethod = "POST")
    @PostMapping("/doLogin") //表单里面用post  RequestBody后面传过来的东西和json对象对应
    public GraceJSONResult doLogin(@RequestBody @Valid RegistLoginBO registLoginBO
            , BindingResult result, HttpServletRequest request, HttpServletResponse response);
    //完成之后 去BaseController里面写一个setCookie()方便都可以用

    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    public GraceJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response);
}
