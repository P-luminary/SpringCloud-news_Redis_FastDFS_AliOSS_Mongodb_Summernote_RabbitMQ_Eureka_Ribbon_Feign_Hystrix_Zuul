package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "首页友情连接维护", tags = {"首页友情连接维护"})
@RequestMapping("friendLinkMng")
public interface FriendLinkControllerApi {

    @ApiOperation(value = "新增或者修改友情连接", notes = "新增或者修改友情连接", httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink")
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody SaveFriendLinkBO saveFriendLinkBO,
                                      BindingResult result);
    @ApiOperation(value = "查询改友情连接列表", notes = "查询改友情连接列表", httpMethod = "POST")
    @PostMapping("/getFriendLinkList")
    public GraceJSONResult getFriendLinkList();

    @ApiOperation(value = "删除改友情连接列表", notes = "删除改友情连接列表", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String linkId);

    @ApiOperation(value = "门户端查询友情链接列表", notes = "门户端查询友情链接列表", httpMethod = "GET")
    @GetMapping("portal/list")
    public GraceJSONResult queryPortalAllFriendLinkList();
}
