package com.ninjabooks.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer
{
    private static final int POOL_SIZE = 4;
    private static final String THREAD_NAME_PREFIX = "nb-scheduler-";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(POOL_SIZE);
        taskScheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
