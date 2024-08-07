package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserAccountInfoVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class UserController extends BaseController implements UserControllerApi {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // 其他方法一旦发现异常就会进入这个方法里面 全局唯一 其他的降级方法要注释
    public GraceJSONResult defaultFallback(){
        return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_GLOBAL);
    }

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        //接口进行解耦!!
        // 0. 判断参数不为空
        if (StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        // 1. 根据userId查询用户的信息 UserService+impl
        AppUser user = getUser(userId);
        // 2. 返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO); //拷贝信息
        // 3. 查询redis中用户的关注数和粉丝数，放入userVO放入前端渲染
        userVO.setMyFansCounts(getCountsFromRedis(REDIS_WRITER_FANS_COUNTS + ":" + userId));
        userVO.setMyFollowCounts(getCountsFromRedis(REDIS_MY_FOLLOW_COUNTS + ":" + userId));
        return GraceJSONResult.ok(userVO);
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        // 0. 判断参数不为空
        if (StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1. 根据userId查询用户的信息 UserService+impl
        AppUser user = getUser(userId);
        // 2. 返回用户信息
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, accountInfoVO); //拷贝信息

        return GraceJSONResult.ok(accountInfoVO);
    }

    private AppUser getUser(String userId){
        //查询判断redis中是否包含用户信息 若有则直接返回就不去查询数据库了
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;
        if (StringUtils.isNotBlank(userJson)){
            //字符串转换成json对象  要提取user 所以要一开始赋值null
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            // TODO 本方法后续公用，并且扩展
            user = userService.getUser(userId);
            // 由于用户信息不怎么会变动,对于一些千万级别网站来说,这类信息不会直接去查询数据库
            // 可以完全依靠Redis,直接把查询后的数据存入到Redis中
            // set里面设置一个key去BaseController里设置  ↓user变成jason转换类
            redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        }

        return user;
    }

    @Override
  public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO){
    //, BindingResult result) {
//        // 0.校验BO
//        if (result.hasErrors()){
//            Map<String, String> map = getErrors(result);
//            return GraceJSONResult.errorMap(map);
//        }
        // 1.执行更新操作
        userService.updateUserInfo(updateUserInfoBO);
        return GraceJSONResult.ok();
        //调用UserService把独有信息传入
    }

    @Value("${server.port}")
    private String myPort;
    // 添加熔断机制 一旦熔断会有替补方法[降级的方法]
    @HystrixCommand//(fallbackMethod = "queryByIdsFallback")
    @Override
    public GraceJSONResult queryByIds(String userIds) {
        // 1.手动触发异常
        int a = 1/0;
        // 2.模拟超时异常
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("myPort=" + myPort);
        if (StringUtils.isBlank(userIds)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);//传过来两个用户的id
        // FIXME: 仅用于dev测试，硬编码动态判断来抛出异常
        if (userIdList.size() > 1){
            System.out.println("出现异常~~");
            throw new RuntimeException("出现异常~~");
        }

        for (String userId : userIdList){
            //获得用户基本信息
            AppUserVO userVO = getBasicUserInfo(userId);
            // 3.添加到publisherList
            publisherList.add(userVO);
        }
        return GraceJSONResult.ok(publisherList);
    }

    public GraceJSONResult queryByIdsFallback(String userIds) {
        System.out.println("进入降级方法：queryByIdsFallback");

        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);//传过来两个用户的id
        for (String userId : userIdList){
            // 手动构建空对象，详情页所展示的用户信息可有可无 返回空对象
            AppUserVO userVO = new AppUserVO();
            publisherList.add(userVO);
        }
        return GraceJSONResult.ok(publisherList);
    }

    private AppUserVO getBasicUserInfo(String userId){
        // 1. 根据userId查询用户的信息 UserService+impl
        AppUser user = getUser(userId);
        // 2. 返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO); //拷贝信息
        return userVO;
    }
}
