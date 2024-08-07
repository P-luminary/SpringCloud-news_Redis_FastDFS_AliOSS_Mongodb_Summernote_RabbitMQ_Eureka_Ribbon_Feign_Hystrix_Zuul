package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.AppUserMngControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.user.service.AppUserMngService;
import com.imooc.user.service.UserService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AppUserMngController extends BaseController implements AppUserMngControllerApi {
    final static Logger logger = LoggerFactory.getLogger(AppUserMngController.class);
// 字符串无法直接转换成Date类型 需要工具类转换 DateConverterConfig com/imooc/api/config/DateConverterConfig.java
    @Autowired
    private AppUserMngService appUserMngService;
    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult queryAll(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        System.out.println(startDate);
        System.out.println(endDate);
        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result = appUserMngService.queryAllUserList(nickname, status, startDate, endDate, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult userDetail(String userId) {
        return GraceJSONResult.ok(userService.getUser(userId));
    }

    @Override
    public GraceJSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if (!UserStatus.isUserStatusValid(doStatus)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        appUserMngService.freezeUserOrNot(userId, doStatus);
        //若冻结后 用户处于登录状态 还可以进行操作 所以要刷新用户状态
        //方法①：删除用户会话，从而保证用户需要重新登陆以后再来刷新她的会话状态
        redis.del(REDIS_USER_INFO + ":" + userId);
        //方法②：查询最新用户的信息，重新放入redis中，做一次更新
        return GraceJSONResult.ok();
    }

}
