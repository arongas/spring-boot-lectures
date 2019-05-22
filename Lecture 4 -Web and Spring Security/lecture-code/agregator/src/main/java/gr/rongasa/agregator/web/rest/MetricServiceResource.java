package gr.rongasa.agregator.web.rest;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/metrics")@RequiredArgsConstructor
public class MetricServiceResource {

    private final MetricService metricService;

    @GetMapping
    public ResponseEntity<Page<MetricDTO>> getMetrics(Pageable page){
        return metricService.getMetrics(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricDTO> getMetric(UUID uuid){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<MetricDTO>> getMetrics(UUID uuid){
        return null;
    }
}
