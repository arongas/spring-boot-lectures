package gr.rongasa.eshop.web.rest;

import gr.rongasa.eshop.domain.Inventory;
import gr.rongasa.eshop.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "inventory", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InventoryResource {
    private final InventoryService inventoryService;

    @GetMapping
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryService.findAll(pageable);
    }

    @GetMapping("name")
    public Page<Inventory> findAllByName(Pageable pageable, @RequestParam("name") String name) {
        return inventoryService.findAllByName(pageable,name);
    }

    @GetMapping("type")
    public Page<Inventory> findAllByType(Pageable pageable, @RequestParam("type") String type) {
        return inventoryService.findAllByType(pageable,type);
    }

    @PostMapping
    public ResponseEntity<Inventory> save(@RequestBody Inventory entity) {
        return ResponseEntity.ok(inventoryService.save(entity));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Inventory> update(@PathVariable("id") String id, @RequestBody Inventory entity) {
        entity.setId(id);
        return ResponseEntity.ok(inventoryService.save(entity));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Inventory> findById(@PathVariable("id") String id) {
        return inventoryService.findById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Inventory> deleteById(@PathVariable("id") String id) {
        return inventoryService.deleteById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
