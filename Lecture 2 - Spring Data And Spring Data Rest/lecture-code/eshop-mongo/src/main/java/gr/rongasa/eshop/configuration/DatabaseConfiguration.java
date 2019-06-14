package gr.rongasa.eshop.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"gr.rongasa.eshop.repository"})
@Slf4j
public class DatabaseConfiguration {

    @Bean
    public CommandLineRunner stupidBean() {
        return args -> log.info("Mongo java code started");
    }
}
