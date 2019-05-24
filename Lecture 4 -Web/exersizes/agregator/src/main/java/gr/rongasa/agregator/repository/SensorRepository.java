package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Sensor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SensorRepository extends ElasticsearchRepository<Sensor, String> {
}
