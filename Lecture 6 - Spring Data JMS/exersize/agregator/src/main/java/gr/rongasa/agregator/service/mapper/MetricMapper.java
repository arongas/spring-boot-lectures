package gr.rongasa.agregator.service.mapper;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.jpa.Metric;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SensorMapper.class})
public interface MetricMapper {


    Metric metricDTOToMetric(MetricDTO metricDTO);

    MetricDTO metricToMetricDTO(Metric metric);


}
