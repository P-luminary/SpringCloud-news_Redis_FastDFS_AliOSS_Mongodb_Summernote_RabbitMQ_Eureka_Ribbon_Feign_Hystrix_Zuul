package com.imooc.api.controller.user.fallbacks;

import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component //这个类让容器加载
public class UserControllerFactoryFallback implements FallbackFactory<UserControllerApi> {
    @Override
    public UserControllerApi create(Throwable throwable) {
 // 重写的过程就是降级的过程
        return new UserControllerApi() {
            //SYSTEM_ERROR_FEIGN(559, false, "客户端Feign降级：系统繁忙，请稍后再试！")
            @Override
            public GraceJSONResult getUserInfo(String userId) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
            }

            @Override
            public GraceJSONResult getAccountInfo(String userId) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
            }

            @Override
            public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_FEIGN);
            }

            @Override
            public GraceJSONResult queryByIds(String userIds) {
                System.out.println("进入客户端(服务调用者)的降级方法");
                List<AppUserVO> publisherList = new ArrayList<>();
                return GraceJSONResult.ok(publisherList);
            }
        };
    }
}
