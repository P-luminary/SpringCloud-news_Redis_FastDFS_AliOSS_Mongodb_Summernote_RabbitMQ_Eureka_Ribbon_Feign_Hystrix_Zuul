package com.imooc.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 的配置类
 */
@Configuration
public class RabbitMQDelayConfig {

    // 定义交换机的名字
    public static final String EXCHANGE_DELAY = "exchange_delay";

    // 定义队列的名字
    public static final String QUEUE_DELAY = "queue_delay";

    // 创建延迟交换机
    @Bean(EXCHANGE_DELAY)
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "topic");
        return new CustomExchange(EXCHANGE_DELAY, "x-delayed-message", true, false, args);
    }

    // 创建队列
    @Bean(QUEUE_DELAY)
    public Queue queue(){
        return new Queue(QUEUE_DELAY);
    }

    // 队列绑定交换机 ↓ binding必须要唯一
    @Bean
    public Binding delayBinding(
            @Qualifier(QUEUE_DELAY) Queue queue,
            @Qualifier(EXCHANGE_DELAY) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("publish.delay.#")
                .noargs();      // 执行绑定
    }
}
