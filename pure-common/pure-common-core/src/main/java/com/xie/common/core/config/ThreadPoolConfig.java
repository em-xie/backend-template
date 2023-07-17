package com.xie.common.core.config;

import com.xie.common.core.config.properties.ThreadPoolProperties;
import com.xie.common.core.factory.MyThreadFactory;
import com.xie.common.core.utils.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author Lion Li
 **/
@AutoConfiguration
@EnableConfigurationProperties(ThreadPoolProperties.class)
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {

    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 项目共用线程池
     */
    public static final String Pure_EXECUTOR = "PureExecutor";

//    /**
//     * websocket通信线程池
//     */
//    public static final String WS_EXECUTOR = "websocketExecutor";
//
//
//    public static final String AICHAT_EXECUTOR = "aichatExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return pureExecutor();
    }

    @Bean(Pure_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor pureExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("pure-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//满了调用线程执行，认为重要任务
        executor.setThreadFactory(new MyThreadFactory(executor));
        executor.initialize();
        return executor;
    }

//    @Bean(WS_EXECUTOR)
//    public ThreadPoolTaskExecutor websocketExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(16);
//        executor.setMaxPoolSize(16);
//        executor.setQueueCapacity(1000);//支持同时推送1000人
//        executor.setThreadNamePrefix("websocket-executor-");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());//满了直接丢弃，默认为不重要消息推送
//        executor.setThreadFactory(new MyThreadFactory(executor));
//        executor.initialize();
//        return executor;
//    }

//    @Bean(AICHAT_EXECUTOR)
//    public ThreadPoolTaskExecutor chatAiExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(15);
//        executor.setThreadNamePrefix("aichat-executor-");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());//满了直接丢弃，默认为不重要消息推送
//        executor.setThreadFactory(new MyThreadFactory(executor));
//        return executor;
//    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(core,
            new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
            new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }
}
