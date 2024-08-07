package com.imooc.user.controller;


import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.user.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.SMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("passport")
public class PassportController extends BaseController implements PassportControllerApi {
    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userService;
    // 这里去除的原因是因为新建了一个BaseController 在里面有信息 且在这加个extends
    //    @Autowired
    //    private RedisOperator redis;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request){
        //获取用户ip
        String userIp = IPUtil.getRequestIp(request);
        logger.info("User ip:", userIp);
        //根据用户的ip进行限制,限制用户在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        // 生成6位随机验证码
        String random = (int)((Math.random() * 9 + 1) * 100000) + "";
        // 打印生成的验证码以便调试
//        logger.info("Generated SMS code: " + random);
//        String random = ((Math.random() * 9 + 1) * 100000) + "";
//        smsUtils.sendSMS("15027597319",random);//可以用MyInfo.getMobile代替
        // 记录发送短信的结果（添加日志）
//        logger.info("SMS sent to 15027597319 with code: " + random);
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult doLogin(RegistLoginBO registLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        //0.判断BindingResult中是否保存了错误的验证信息 如果有则需要返回
        if (result.hasErrors()){
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();

        //1.校验验证码是否匹配[在redis中去获取]
        String redisSMSCode = redis.get(MOBILE_SMSCODE + ":" + mobile); //为空||不同值
        if (StringUtils.isBlank(redisSMSCode) || !redisSMSCode.equalsIgnoreCase(smsCode)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        //2.查询数据库,判断该用户注册
        AppUser user = userService.queryMobileIsExist(mobile);
        if (user != null && user.getActiveStatus() == UserStatus.FROZEN.type){
            //如果用户不为空，并且状态为冻结，则直接抛出异常，禁止登录
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }else if (user == null){
            //如果用户没有注册过，则为null，需要注册信息入库
            user = userService.createUser(mobile);
        }

        // 3.保存用户分布式会话的相关操作
        int userActiveStatus = user.getActiveStatus();
        if (userActiveStatus != UserStatus.FROZEN.type){
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN+":"+user.getId(),uToken);//BaseController里面 保存token到redis
            redis.set(REDIS_USER_INFO+":"+user.getId(), JsonUtils.objectToJson(user));

            //保存用户id和token到cookie中 设计一个request response 回到PassportControllerApi
            setCookie(request, response,"utoken",uToken,COOKIE_MONTH);
            setCookie(request, response,"uid",user.getId(),COOKIE_MONTH);
        }
        // 4.用户登录或注册成功以后，需要删除redis中的短信验证码，验证码只能使用一次，用过则作废
//        redis.del(MOBILE_SMSCODE + ":" + mobile);
        // 5.返回用户状态 返回前端看
        return GraceJSONResult.ok(userActiveStatus);
    }

    @Override
    public GraceJSONResult logout(String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        redis.del(REDIS_USER_TOKEN + ":" + userId);
        //USER_INFO可以不用删 可能后面会查询 没有清除cookie只有重新设置时间为0
        setCookie(request, response, "utoken","",COOKIE_DELETE);
        setCookie(request, response, "uid","",COOKIE_DELETE);
        return GraceJSONResult.ok();
    }
}
