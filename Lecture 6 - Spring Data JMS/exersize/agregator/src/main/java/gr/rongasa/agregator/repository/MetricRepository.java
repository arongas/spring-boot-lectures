package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface MetricRepository extends ElasticsearchRepository<Metric, UUID> {
    Page<Metric> findBySensorSensorId(Pageable page, String sensorId);
}
