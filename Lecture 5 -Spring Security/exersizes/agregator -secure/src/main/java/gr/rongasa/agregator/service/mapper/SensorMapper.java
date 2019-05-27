package gr.rongasa.agregator.service.mapper;

import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.domain.jpa.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "metrics", ignore = true)
    Sensor sensorDTOToSensor(SensorDTO sensorDTO);

    SensorDTO sensorToSensorDTO(Sensor sensor);

}
