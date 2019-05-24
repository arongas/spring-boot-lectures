package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.web.exception.SensorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SensorService {
    Page<SensorDTO> getSensors(Pageable page);

    Optional<SensorDTO> getSensor(String sensorId);

    SensorDTO saveSensor(SensorDTO sensor) throws SensorException;

    SensorDTO updateSensor(String sensorId, SensorDTO sensor) throws SensorException;
}
