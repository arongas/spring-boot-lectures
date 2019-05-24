package gr.rongasa.sensor.web.rest;

import gr.rongasa.sensor.domain.MetricDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
public class MeasurementResource {
    //TODO: should be sen externally or via config, or sensor mac, etc
    @Value("${server.port}")
    String sensorId;

    @GetMapping
    public ResponseEntity<List<MetricDTO>> getMeasurements(HttpServletRequest request) {
        List<MetricDTO> metricDTOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            metricDTOList.add(MetricDTO.builder().id(UUID.randomUUID()).sensorId(sensorId).time(Instant.now()).value(ThreadLocalRandom.current().nextDouble() + "").build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                //
            }
        }
        return ResponseEntity.ok(metricDTOList);
    }
}
