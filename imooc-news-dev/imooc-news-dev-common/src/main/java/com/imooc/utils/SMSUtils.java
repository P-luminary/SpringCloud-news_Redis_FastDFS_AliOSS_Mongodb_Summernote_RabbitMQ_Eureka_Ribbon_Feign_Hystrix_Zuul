package com.imooc.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.imooc.utils.extend.AliyunResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //工具类 可以作为组件
public class SMSUtils {
    @Autowired
    public AliyunResource aliyunResource;
    final static Logger logger = LoggerFactory.getLogger(SMSUtils.class);
    public void sendSMS(String mobile, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                aliyunResource.getAccessKeyID(),
                aliyunResource.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        //给对方发送的手机号
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "小潘科技");//控制台可以添加签名
        request.putQueryParameter("TemplateCode", "SMS_467115116");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");//JSON对象字符串
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            // 打印阿里云API的响应结果
            logger.info("Aliyun SMS API response: " + response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
            logger.error("ServerException: " + e.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
            logger.error("ClientException: " + e.getMessage());
        }
    }
}
