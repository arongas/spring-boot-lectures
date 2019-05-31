package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MetricService {
    Page<MetricDTO> getMetrics(Pageable page);

    Optional<MetricDTO> getMetric(UUID uuid);

    Page<MetricDTO> getMetricsBySensorId(Pageable page, String sensorId);

    MetricDTO save(MetricDTO metrics);
}
