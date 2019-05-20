package gr.rongasa.library.service;

import gr.rongasa.library.domain.dto.ResourceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ResourceService {
    Page<ResourceDTO> findAll(Pageable pageable);

    Optional<ResourceDTO> findById(String trackingId);

    Page<ResourceDTO> findByType(Pageable pageable, String trackingId);

    Page<ResourceDTO> findByName(Pageable pageable, String name);

    void deleteById(String trackingId);

    Optional<ResourceDTO> save(ResourceDTO resourceDTO);

    Optional<ResourceDTO> update(ResourceDTO resourceDTO);

    void reSynch();
}
