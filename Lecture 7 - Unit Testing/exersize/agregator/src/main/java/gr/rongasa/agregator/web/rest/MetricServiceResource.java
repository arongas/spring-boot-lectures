package gr.rongasa.agregator.web.rest;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/metric")
@RequiredArgsConstructor
public class MetricServiceResource {

    private final MetricService metricService;

    @GetMapping
    public ResponseEntity<Page<MetricDTO>> getMetrics(Pageable page) {
        return ResponseEntity.ok(metricService.getMetrics(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricDTO> getMetric(@PathVariable("id") UUID uuid) {
        return metricService.getMetric(uuid).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("sensor/{id}")
    public ResponseEntity<Page<MetricDTO>> getMetricsBySensor(Pageable page, @PathVariable("id") String sensorId) {
        return ResponseEntity.ok(metricService.getMetricsBySensorId(page, sensorId));
    }
}
