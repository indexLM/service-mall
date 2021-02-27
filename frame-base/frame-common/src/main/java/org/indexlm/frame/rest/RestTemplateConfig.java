package org.indexlm.frame.rest;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置类
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Configuration
public class RestTemplateConfig {
    public static final int TIMEOUT = 20 * 1000;

    /**
     * 配置RestTemplate
     *
     * @return {@link RestTemplate}
     * @author LiuMing
     * @since 2021/2/27
     */

    @Bean(name = "restTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(TIMEOUT);
        httpRequestFactory.setConnectTimeout(TIMEOUT);
        httpRequestFactory.setReadTimeout(TIMEOUT);
        return new RestTemplate(httpRequestFactory);
    }
    /**
     * 基于OkHttp3配置RestTemplate
     *
     * @return {@link RestTemplate}
     * @author LiuMing
     * @date 2020/9/29
     */
    @Bean(name = "feignRestTemplate")
    @LoadBalanced
    public RestTemplate feignRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(TIMEOUT);
        httpRequestFactory.setConnectTimeout(TIMEOUT);
        httpRequestFactory.setReadTimeout(TIMEOUT);
        return new RestTemplate(httpRequestFactory);

    }

}
