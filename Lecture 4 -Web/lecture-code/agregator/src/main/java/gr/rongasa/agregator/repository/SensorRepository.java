package gr.rongasa.agregator.repository;

import gr.rongasa.agregator.domain.jpa.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
