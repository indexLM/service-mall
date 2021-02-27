package org.indexlm.frame.log;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * aop切面日志新增json参数打印和高并发场景
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Slf4j
@Aspect
@Component(value = "logInfo")
public class AopLog {

    private static final String UNKNOWN = "unknown";
    public static final String USER_AGENT = "User-Agent";
    @Value("${spring.application.name}")
    private String serverName;

    /**
     * 切入点
     */
    @Pointcut("@annotation(org.indexlm.frame.log.LogPrint) ")
    public void log() {

    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     * @author LiuMing
     * @since 2021/2/27
     */
    @Around("@annotation(logPrint)")
    public Object aroundLog(ProceedingJoinPoint point, LogPrint logPrint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        // 打印请求相关参数
        long startTime = System.currentTimeMillis();
        Object result;
        Throwable tempThrowable = null;
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            result = throwable.getMessage();
            tempThrowable = throwable;
        }
        String agentHeader = request.getHeader(USER_AGENT);
        UserAgent userAgent;
        if (StrUtil.isNotBlank(agentHeader)) {
            userAgent = UserAgent.parseUserAgentString(agentHeader);
        } else {
            userAgent = null;
        }
        Log l = Log.builder()
                .value(logPrint.value())
                .httpMethod(request.getMethod())
                .url(request.getRequestURL().toString())
                .requestParams(getNameAndValue(point))
                .result(result == null ? "null" : result)
                .timeCost(System.currentTimeMillis() - startTime)

                .traceId("traceHeader")
                .traceDegree(1)

                .classMethod(String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
                        point.getSignature().getName()))
                .ip(getIp(request))

                .userAgent(agentHeader)
                .browser(userAgent == null ? UNKNOWN : userAgent.getBrowser().toString())
                .os(userAgent == null ? UNKNOWN : userAgent.getOperatingSystem().toString())

                .serverIp(InetAddress.getLocalHost().getHostAddress())
                .serverName(serverName)
                .remark(logPrint.remark())
                .build();
        log.info("\n==============================================================\n" +
                        "接口日志 : {}\n" +
                        "请求方式 : {}\n" +
                        "请求接口路径 : {}\n" +
                        "请求参数 : {}\n" +
                        "接口响应结果 : {}\n" +
                        "接口运行时间 : {}\n" +

                        "请求唯一ID : {}\n" +
                        "请求深度 : {}\n" +

                        "请求接口方法名称 ：{}\n" +
                        "请求IP地址 : {}\n" +

                        "userAgent : {}\n" +
                        "浏览器 : {}\n" +
                        "操作系统 : {}\n" +

                        "服务实例IP : {}\n" +
                        "服务名称 : {}\n" +
                        "备注 : {}\n",
                l.getValue(), l.getHttpMethod(), l.getUrl(), l.getRequestParams(), l.getResult(), l.getTimeCost(),
                l.getTraceId(), l.getTraceDegree(),
                l.getClassMethod(), l.getIp(),
                l.getUserAgent(), l.getBrowser(), l.getOs(),
                l.getServerIp(), l.getServerName(), l.getRemark()
        );
        if (tempThrowable != null) {
            throw tempThrowable;
        }
        return result;
    }

    /**
     * 获取方法参数名和参数值
     *
     * @param joinPoint {@link ProceedingJoinPoint}
     * @return {@link Map<String,Object>}
     * @author LiuMing
     * @since 2021/2/27
     */
    private Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {
        final Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        final String[] names = methodSignature.getParameterNames();
        final Object[] args = joinPoint.getArgs();
        if (ArrayUtil.isEmpty(names) || ArrayUtil.isEmpty(args)) {
            return Collections.emptyMap();
        }
        if (names.length != args.length) {
            log.warn("{}方法参数名和参数值数量不一致", methodSignature.getName());
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], args[i]);
        }
        return map;
    }

    /**
     * 获取ip地址
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/27
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * 日志类
     *
     * @author LiuMing
     * @since 2021/2/27
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Log {
        // 线程id
        private String value;
        // 线程id
        private String remark;
        private String traceId;
        private Integer traceDegree;
        // 线程id
        private String threadId;
        // 线程名称
        private String threadName;
        // ip
        private String ip;
        //服务IP
        private String serverIp;
        //服务名称
        private String serverName;
        // url
        private String url;
        // http方法 GET POST PUT DELETE PATCH
        private String httpMethod;
        // 类方法
        private String classMethod;
        // 请求参数
        private Object requestParams;
        // 返回参数
        private Object result;
        // 接口耗时
        private Long timeCost;
        // 操作系统
        private String os;
        // 浏览器
        private String browser;
        // user-agent
        private String userAgent;
    }
}
