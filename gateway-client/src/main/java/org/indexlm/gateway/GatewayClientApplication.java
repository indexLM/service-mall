package org.indexlm.gateway;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

/**
 * @author LiuMing
 * @since 2021/2/27
 */
@SpringCloudApplication
@MapperScan(basePackages = "org.indexlm.gateway")
@ComponentScan({"org.indexlm.gateway",
       })
public class GatewayClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayClientApplication.class, args);
    }

    @Bean
    @Scope(value="prototype")
    public IRule loadBalanceRule(){
        NacosRule nacosRule = new NacosRule();
        return nacosRule;
    }

}
