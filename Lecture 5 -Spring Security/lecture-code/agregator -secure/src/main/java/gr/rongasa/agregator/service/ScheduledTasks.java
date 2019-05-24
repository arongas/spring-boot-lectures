package gr.rongasa.agregator.service;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.dto.SensorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class ScheduledTasks {
    private final SensorService sensorService;
    private final MetricService metricService;
    private final ExecutorService sensorsExecutorService;
    private final RestTemplate restTemplate;


    @Scheduled(fixedRate = 3600)
    public void collectMetrics() {
        int pageNumber = 0;
        Page<SensorDTO> page;
        do {
            page = sensorService.getSensors(PageRequest.of(pageNumber++, 50));
            page.stream().forEach(sensor ->
                    sensorsExecutorService.submit(() -> collectMetricsForSensor(sensor))
            );
        } while (page.hasNext());
    }

    private void collectMetricsForSensor(SensorDTO sensor) {
        //todo add pagination
        ResponseEntity<List<MetricDTO>> response = restTemplate.exchange(
                sensor.getLocation(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MetricDTO>>() {
                });
        if (response.getBody()!=null){
            response.getBody().forEach(metricService::save);
        }
    }
}
