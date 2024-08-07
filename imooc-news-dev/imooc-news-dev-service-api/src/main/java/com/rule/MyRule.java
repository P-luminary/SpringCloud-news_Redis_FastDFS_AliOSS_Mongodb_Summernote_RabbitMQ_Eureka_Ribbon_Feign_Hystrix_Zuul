package com.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 官方定义了规则不要被 @ComponentScan( 扫描到
@Configuration
public class MyRule {
    @Bean
    public IRule iRule(){//随机的负载均衡策略
        return new RandomRule();
        // 在调用方article的启动类开启注解 @RibbonClient
    }
}
