package gr.rongasa.library.service;

import gr.rongasa.library.domain.jpa.JpaResource;
import gr.rongasa.library.repository.elasticsearch.ResourceRepository;
import gr.rongasa.library.repository.jpa.JpaResourceRepository;
import gr.rongasa.library.service.mapper.elasticsearch.ResourceMapper;
import gr.rongasa.library.service.mapper.jpa.JpaResourceMapper;
import gr.rongasa.library.domain.dto.ResourceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl {
    public final ResourceRepository resourceRepository;
    public final JpaResourceRepository jpaResourceRepository;

    public final ResourceMapper resourceMapper;
    public final JpaResourceMapper jpaResourceMapper;

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
        jpaResourceRepository.deleteById(trackingId);
    }

    public Optional<ResourceDTO> save(ResourceDTO resourceDTO) {
        if (!resourceRepository.findById(resourceDTO.getTrackingId()).isPresent()) {
            jpaResourceRepository.save(jpaResourceMapper.resourceDTOToResource(resourceDTO));
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


    public void reSynch() {
        int page=0;
        Page<JpaResource> resourcePage=null;
        do {
            Pageable pageable= PageRequest.of(page++,10);
            resourcePage=jpaResourceRepository.findAll(pageable);
            resourceRepository.saveAll(resourcePage.stream().map(jpaResourceMapper::resourceToResourceDTO).map(resourceMapper::resourceDTOToResource).collect(Collectors.toList()));
        }while (resourcePage.hasNext());

    }
}
