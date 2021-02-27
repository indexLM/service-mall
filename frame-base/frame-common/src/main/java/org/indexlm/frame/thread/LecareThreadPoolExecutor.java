package org.indexlm.frame.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程提交时 打印线程池信息，如线程名称，核心线程数，提交的任务数量
 *
 * @author LiuMing
 * @since 2021/2/27
 */
public class LecareThreadPoolExecutor extends ThreadPoolTaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(LecareThreadPoolExecutor.class);

    private void showThreadPoolInfo(){
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        if(null == threadPoolExecutor){
            return;
        }

        logger.info("活跃线程数:[{}], 核心线程数:[{}],最大线程数 [{}], 线程池活跃线程数量 [{}], 任务数量 [{}], 已提交任务数量 [{}]，队列等待数量[{}]",
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getCorePoolSize(),
                threadPoolExecutor.getMaximumPoolSize(),
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo();
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo();
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo();
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo();
        return super.submit(task);
    }
}
