package gr.rongasa.agregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SensorTasksConfig {

    @Bean
    public ExecutorService sensorsExecutorService() {
        return Executors.newFixedThreadPool(10);
    }
}
