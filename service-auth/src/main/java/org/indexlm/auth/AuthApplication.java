package org.indexlm.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证服务启动类
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@SpringCloudApplication
@EntityScan("org.indexlm.frame.bean")
@MapperScan(basePackages = "org.indexlm.auth.dao")
@ComponentScan(basePackages = {
        "org.indexlm.auth"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
