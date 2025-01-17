package com.imooc.article.stream;

import com.imooc.pojo.AppUser;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 构建消费端
 */
@Component
@EnableBinding(MyStreamChannel.class) //开启通道绑定
public class MyStreamConsumer {

    /**
     * 监听并且实现消息的消费和相关业务处理
     */
    @StreamListener(MyStreamChannel.INPUT)
    public void receiveMsg(AppUser user) {
        System.out.println(user.toString());
    }

    @StreamListener(MyStreamChannel.INPUT)
    public void receiveMsg(String dumpling) {
        System.out.println(dumpling);
    }

}
