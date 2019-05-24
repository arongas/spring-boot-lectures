package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.repository.SensorRepository;
import gr.rongasa.agregator.service.mapper.SensorMapper;
import gr.rongasa.agregator.web.exception.SensorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;

    @Override
    public Page<SensorDTO> getSensors(Pageable page) {
        return sensorRepository.findAll(page).map(sensorMapper::sensorToSensorDTO);
    }

    @Override
    public Optional<SensorDTO> getSensor(String sensorId) {
        return sensorRepository.findById(sensorId).map(sensorMapper::sensorToSensorDTO);
    }

    @Override
    public SensorDTO saveSensor(SensorDTO sensor) throws SensorException {
        if (!sensorRepository.findById(sensor.getSensorId()).isPresent()) {
            return sensorMapper.sensorToSensorDTO(sensorRepository.save(sensorMapper.sensorDTOToSensor(sensor)));
        } else {
            throw new SensorException("Sensor already exists");
        }
    }

    @Override
    public SensorDTO updateSensor(String sensorId, SensorDTO sensor) throws SensorException {
        if (sensorRepository.findById(sensorId).isPresent()) {
            return sensorMapper.sensorToSensorDTO(sensorRepository.save(sensorMapper.sensorDTOToSensor(sensor)));
        } else {
            throw new SensorException("Sensor "+sensorId+" does not exist");
        }
    }
}
