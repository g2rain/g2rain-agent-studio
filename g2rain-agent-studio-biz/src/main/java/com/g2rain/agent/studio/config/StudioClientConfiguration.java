package com.g2rain.agent.studio.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * 给跨服务调用准备负载均衡的 {@link RestClient}。
 *
 * @author alpha
 * @since 2026/10/16
 */
@Configuration
public class StudioClientConfiguration {

    /**
     * 解析 {@code http://服务名}。容器里已有负载均衡客户端时不再注册。
     */
    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean(annotation = LoadBalanced.class)
    RestClient.Builder lbRestClientBuilder() {
        return RestClient.builder();
    }
}
