package com.azminds.podcastparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name ="taskExecutor")
  public Executor taskExecutor(){
    ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(100);
    executor.setMaxPoolSize(100);
    executor.setKeepAliveSeconds(3000);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("podcastThread-");
    executor.initialize();
    return executor;
  }
}
