package gr.rongasa.eshop.repository;

import gr.rongasa.eshop.domain.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface InventoryRepository extends ElasticsearchRepository<Inventory,String> {
    Page<Inventory> findAllByType(Pageable pageable, String type);
    Page<Inventory> findAllByName(Pageable pageable, String type);
}
