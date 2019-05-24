package gr.rongasa.agregator.web.rest;

import gr.rongasa.agregator.domain.dto.SensorDTO;
import gr.rongasa.agregator.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
public class SensorServiceResource {

    private final SensorService sensorService;

    @GetMapping
    public ResponseEntity<Page<SensorDTO>> getSensors(Pageable page) {
        return ResponseEntity.ok(sensorService.getSensors(page));
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable("sensorId") String sensorId) {
        return sensorService.getSensor(sensorId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SensorDTO> saveSensor(@RequestBody @Valid SensorDTO sensor) {
        return ResponseEntity.ok(sensorService.saveSensor(sensor));
    }

    @PutMapping("/{sensorId}")
    public ResponseEntity<SensorDTO> saveSensor(@PathVariable("sensorId") String sensorId, @Valid @RequestBody SensorDTO sensor) {
        return ResponseEntity.ok(sensorService.updateSensor(sensorId, sensor));
    }
}
