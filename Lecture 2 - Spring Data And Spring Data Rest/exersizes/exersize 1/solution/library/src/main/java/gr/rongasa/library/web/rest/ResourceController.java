package gr.rongasa.library.web.rest;

import gr.rongasa.library.service.ResourceServiceImpl;
import gr.rongasa.library.domain.dto.ResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "resource", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ResourceController {
    public final ResourceServiceImpl resourceService;

    @GetMapping
    public ResponseEntity<Page<ResourceDTO>> resources(Pageable pageable) {
        return ResponseEntity.ok(resourceService.findAll(pageable));
    }

    @GetMapping("name")
    public ResponseEntity<Page<ResourceDTO>> findByName(Pageable pageable, @RequestParam("name") String name) {
        return ResponseEntity.ok(resourceService.findByName(pageable, name));
    }

    @GetMapping("type")
    public ResponseEntity<Page<ResourceDTO>> findByType(Pageable pageable, @RequestParam("type") String type) {
        return ResponseEntity.ok(resourceService.findByType(pageable, type));
    }

    @GetMapping("id")
    public ResponseEntity<ResourceDTO> findById(@Valid @RequestParam("id") String id) {
        return resourceService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("id") String id) {
        resourceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ResourceDTO> save(@Valid @RequestBody ResourceDTO resource) {
        if (resourceService.findById(resource.getTrackingId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            return resourceService.save(resource).map(ResponseEntity::ok).orElse(ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
        }
    }

    @PutMapping
    public ResponseEntity<ResourceDTO> update(@Valid @RequestBody ResourceDTO resource) {
        if (!resourceService.findById(resource.getTrackingId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            return resourceService.update(resource).map(ResponseEntity::ok).orElse(ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
        }
    }
}
