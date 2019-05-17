package gr.rongasa.eshop.service;

import gr.rongasa.eshop.domain.Inventory;
import gr.rongasa.eshop.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    @Override
    public Page<Inventory> findAllByName(Pageable pageable, String name) {
        return inventoryRepository.findAllByName(pageable, name);
    }

    @Override
    public Page<Inventory> findAllByType(Pageable pageable, String type) {
        return inventoryRepository.findAllByType(pageable, type);
    }

    @Override
    public Inventory save(Inventory entity) {
        return inventoryRepository.save(entity);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Optional<Inventory> deleteById(Long id) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventoryRepository.delete(inventory);
            return Optional.of(inventory);
        }).orElseGet(Optional::empty);
    }
}
