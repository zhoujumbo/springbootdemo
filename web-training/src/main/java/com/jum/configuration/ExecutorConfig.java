package com.jum.configuration;


import com.jum.common.logger.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean("threadPoolExecutor")
    public TaskExecutor threadPoolExecutor() {
        logger.info("start threadPoolExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("back-thread-");
        executor.setKeepAliveSeconds(60);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        initDefaultThread(executor, "threadPoolExecutor");
        executor.initialize();
        return executor;
    }

    private void initDefaultThread(ThreadPoolTaskExecutor executor, String taskExecutorName) {
        executor.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setName(taskExecutorName + "-" + thread.getId());
            thread.setUncaughtExceptionHandler((t, e) -> LogUtil.error("线程异常,线程名:{}", t.getName()));
            return thread;
        });
    }
}
