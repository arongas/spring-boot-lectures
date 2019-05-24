package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.repository.MetricRepository;
import gr.rongasa.agregator.service.mapper.MetricMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MetricServiceImpl implements MetricService {

    public final MetricRepository metricRepository;
    public final MetricMapper metricMapper;

    @Override
    public Page<MetricDTO> getMetrics(Pageable page) {
        return metricRepository.findAll(page).map(metricMapper::metricToMetricDTO);
    }

    @Override
    public Optional<MetricDTO> getMetric(UUID uuid) {
        return metricRepository.findById(uuid).map(metricMapper::metricToMetricDTO);
    }

    @Override
    public Page<MetricDTO> getMetricsBySensorId(Pageable page, String sensorId) {
        return metricRepository.findBySensorSensorId(page, sensorId).map(metricMapper::metricToMetricDTO);
    }

    @Override
    public MetricDTO save(MetricDTO metric) {
        return metricMapper.metricToMetricDTO(metricRepository.save(metricMapper.metricDTOToMetric(metric)));
    }


}
