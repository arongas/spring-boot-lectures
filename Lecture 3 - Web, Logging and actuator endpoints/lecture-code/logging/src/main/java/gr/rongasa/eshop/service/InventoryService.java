package gr.rongasa.eshop.service;

import gr.rongasa.eshop.domain.Inventory;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InventoryService {

    Page<Inventory> findAll(Pageable pageable);

    Page<Inventory> findAllByType(Pageable pageable, String type);

    Page<Inventory> findAllByName(Pageable pageable, String name);

    Inventory save(Inventory entity);

    Optional<Inventory> findById(String id);

    Optional<Inventory>  deleteById(String id);
}
