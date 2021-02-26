package org.indexlm.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.indexlm.auth.filter.MyUsernamePasswordAuthenticationFilter;
import org.indexlm.frame.bean.common.AuthCode;
import org.indexlm.frame.bean.common.response.RespRes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * springSecurity的配置类
 *
 * @author LiuMing
 * @since 2021/2/25
 **/
//@EnableWebSecurity //启用Web安全的注解
//@Order(-1)
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "objectMapper")
    private ObjectMapper objectMapper;

    /**
     * 认证资源管理器 对象放入容器中 ，主要是Oauth2认证中密码模式使用
     */
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 采用bcrypt对密码进行编码
     */
    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 指定认证业务
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/configuration/security",
                "/v2/api-docs",//swagger api json
                "/swagger-resources/configuration/ui",//用来获取支持的动作
                "/swagger-resources",//用来获取api-docs的URI
                "/swagger-resources/configuration/security",//安全选项
                "/swagger-ui.html",
                "/webjars/**",
                "/course/coursebase/**",

                "/token/**"

        );
    }


    /**
     * permitAll() 表示放开和登陆有关的接口
     * rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
     * access(String) 如果给定的SpEL表达式计算结果为true，就允许访问
     * 复写这个方法来配置 {@link HttpSecurity}.
     * 通常，子类不能通过调用 super 来调用此方法，因为它可能会覆盖其配置。 默认配置为：
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     **/
    @Override
    public void configure(HttpSecurity http) throws Exception {
/*        http.csrf().disable()    //关闭csrg
                .httpBasic()      //采用httpbasic认证
                .and()
                .formLogin()    //开启表单登陆
                .and()
                .authorizeRequests().anyRequest().authenticated(); //表示剩下的任何请求只要验证之后都可以访问*/
        http.csrf().disable()
                .authorizeRequests()
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .and()
                .exceptionHandling()
                //重写方法，用来解决匿名用户访问无权限资源时的异常
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
//                        resp.setContentType("application/json;charset=utf-8");
//                        PrintWriter out = resp.getWriter();
//                        ResponseResult<Object> objectResponseResult = null;
//                        if (authException instanceof InsufficientAuthenticationException) {
//                            objectResponseResult = new ResponseResult<>(CommonCode.UNAUTHENTICATED);
//                        }
//                        out.write(new ObjectMapper().writeValueAsString(objectResponseResult));
//                        out.flush();
//                        out.close();
//                    }
//                })
                //用来解决认证过的用户访问无权限资源时的异常
                .accessDeniedHandler((req, resp, authException) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    RespRes res = RespRes.code(AuthCode.UN_AUTHORISE);
                    out.write(new ObjectMapper().writeValueAsString(res));
                    out.flush();
                    out.close();
                }).and().addFilterAt(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    MyUsernamePasswordAuthenticationFilter myAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                RespRes res;
                if (e.getMessage().indexOf("UserDetailsService returned null") >= 0) {
                    res = RespRes.code(AuthCode.USERNAME_NOT_EXIST);
                }else if (e.getMessage().indexOf("用户名或密码错误") >= 0) {
                    res = RespRes.code(AuthCode.PASSWORD_ERROR);
                }else {
                    res = RespRes.code(AuthCode.LOGIN_FAILED);
                }
                response.setContentType("application/json;charset=utf-8");
                String responseResultStr = objectMapper.writeValueAsString(res);
                response.getWriter().write(responseResultStr);
            }
        });
        return filter;
    }
}

