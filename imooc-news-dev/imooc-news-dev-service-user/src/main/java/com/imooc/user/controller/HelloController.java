package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {
    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RedisOperator redis;
    public Object hello(){
        logger.debug("debug: hello~");
        logger.info("info: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");

//        return "hello";
//        return IMOOCJSONResult.ok();
//        return IMOOCJSONResult.ok("hello!");
//        return IMOOCJSONResult.errorMsg("您的信息有误");
        return GraceJSONResult.errorCustom(ResponseStatusEnum.SUCCESS);
    }

    @GetMapping("/redis")
    public Object redis(){
        redis.set("age", "18");
        return GraceJSONResult.ok(redis.get("age"));
    }
}
