package com.imooc.api.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CloudConfig {

    public CloudConfig() {
    }

    /**
     * 会基于OKHttp3的配置来实例RestTemplate
     * @return
     */
    @Bean
    @LoadBalanced //添加负载均衡 默认的负载均衡算法：枚举
    public RestTemplate restTemplate() {

        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
