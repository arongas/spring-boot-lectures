package gr.rongasa.agregator.service.mapper;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.jpa.Metric;
import gr.rongasa.agregator.repository.SensorRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {SensorRepository.class})
public abstract class MetricMapper {

    @Autowired
    private SensorRepository sensorRepository;
    @Mapping(target = "sensor", ignore = true)
    public abstract Metric metricDTOToMetric(MetricDTO metricDTO);

    @Mapping(target = "sensorId", source = "sensor.sensorId")
    public abstract MetricDTO metricToMetricDTO(Metric metric);


    @AfterMapping
    void addBackReference(@MappingTarget Metric metric, MetricDTO metricDTO) {
        if (!Objects.isNull(metricDTO.getSensorId())) {
            sensorRepository.findById(metricDTO.getSensorId()).ifPresent(metric::setSensor);
        }
    }

}
