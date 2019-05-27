package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetricRepository extends JpaRepository<Metric, UUID> {
    Page<Metric> findBySensorSensorId(Pageable page, String sensorId);
}
