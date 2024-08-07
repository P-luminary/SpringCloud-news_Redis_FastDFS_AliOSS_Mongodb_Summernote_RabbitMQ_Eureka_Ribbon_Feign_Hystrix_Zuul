package com.imooc.api.controller.user;


import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Api(value = "用户管理相关的接口定义",tags = {"用户管理相关功能的controller"})
@RequestMapping("appUser")
public interface AppUserMngControllerApi {
    @ApiOperation(value = "查询所有网站用户",notes = "查询所有网站用户",httpMethod = "POST")
    @PostMapping("queryAll")
    public GraceJSONResult queryAll(@RequestParam String nickname,
                                    @RequestParam Integer status,
                                    @RequestParam Date startDate,
                                    @RequestParam Date endDate,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize);

    @ApiOperation(value = "查看用户详情",notes = "查看用户详情",httpMethod = "POST")
    @PostMapping("userDetail")
    public GraceJSONResult userDetail(@RequestParam String userId);

    @ApiOperation(value = "冻结用户或者解冻用户",notes = "冻结用户或者解冻用户",httpMethod = "POST")
    @PostMapping("freezeUserOrNot")
    public GraceJSONResult freezeUserOrNot(@RequestParam String userId,@RequestParam Integer doStatus);
}
