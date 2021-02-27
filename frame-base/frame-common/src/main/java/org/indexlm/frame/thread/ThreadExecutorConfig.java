package org.indexlm.frame.thread;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 这是线程池配置
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@EnableAsync
@Configuration
public class ThreadExecutorConfig {

    @Autowired
    private TaskExecutionProperties taskExecutionProperties;

    /**
     * 配置线程池信息
     * CorePoolSize 核心线程5
     * MaxPoolSize 最大线程数15
     * threadNamePrefix 线程名前缀
     * RejectedExecutionHandler 拒绝策略 被拒绝的任务在主线程中运行
     *
     * @author LiuMing
     * @since 2021/2/27
     */
    @Bean("myThread")
    public Executor myThread() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
        executor.setThreadNamePrefix("Async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 是否需要钩子
     *
     * @author LiuMing
     * @since 2021/2/27
     */
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}

