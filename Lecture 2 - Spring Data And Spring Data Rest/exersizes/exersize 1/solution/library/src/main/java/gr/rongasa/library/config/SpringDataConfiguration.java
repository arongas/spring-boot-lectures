package gr.rongasa.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(value = "gr.rongasa.library.repository.jpa")
@EnableElasticsearchRepositories (value = "gr.rongasa.library.repository.elasticsearch")
public class SpringDataConfiguration {
}
