package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Metric;
import gr.rongasa.agregator.domain.jpa.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
