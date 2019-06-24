package gr.rongasa.eshop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"gr.rongasa.eshop.repository"})
public class ElasticSearchConfiguration {
}
