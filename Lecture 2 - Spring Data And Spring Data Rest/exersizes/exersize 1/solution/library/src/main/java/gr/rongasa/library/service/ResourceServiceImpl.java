package gr.rongasa.library.service;

import gr.rongasa.library.repository.ResourceRepository;
import gr.rongasa.library.service.mapper.ResourceMapper;
import gr.rongasa.library.web.dto.ResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl {
    public final ResourceRepository resourceRepository;
    public final ResourceMapper resourceMapper;

    public Page<ResourceDTO> findAll(Pageable pageable) {
        return resourceRepository.findAll(pageable).map(resourceMapper::resourceToResourceDTO);
    }

    public Optional<ResourceDTO> findById(String trackingId) {
        return resourceRepository.findById(trackingId).map(resourceMapper::resourceToResourceDTO);
    }

    public Page<ResourceDTO> findByType(Pageable pageable, String trackingId) {
        return resourceRepository.findByType(pageable, trackingId).map(resourceMapper::resourceToResourceDTO);
    }

    public Page<ResourceDTO> findByName(Pageable pageable, String name) {
        return resourceRepository.findByName(pageable, name).map(resourceMapper::resourceToResourceDTO);
    }

    public void deleteById(String trackingId) {
        resourceRepository.deleteById(trackingId);
    }

    public Optional<ResourceDTO> save(ResourceDTO resourceDTO) {
        if (!resourceRepository.findById(resourceDTO.getTrackingId()).isPresent()) {
            return Optional.of(resourceMapper.resourceToResourceDTO(resourceRepository.save(resourceMapper.resourceDTOToResource(resourceDTO))));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ResourceDTO> update(ResourceDTO resourceDTO) {
        if (resourceRepository.findById(resourceDTO.getTrackingId()).isPresent()) {
            return Optional.of(resourceMapper.resourceToResourceDTO(resourceRepository.save(resourceMapper.resourceDTOToResource(resourceDTO))));
        } else {
            return Optional.empty();
        }
    }
}
