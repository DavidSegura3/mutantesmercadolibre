package com.mercadolibre.mutant.restapi.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration 
{
	private static final Logger logger = LoggerFactory.getLogger(AsyncConfiguration.class);
	
	public Executor taskExecutor()
	{
		logger.debug("creando tarea asíncrona");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("HumanoThread-");
        executor.initialize();
        return executor;
	}
}