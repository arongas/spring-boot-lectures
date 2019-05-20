package gr.rongasa.library.service.mapper.jpa;

import gr.rongasa.library.domain.jpa.JpaResource;
import gr.rongasa.library.domain.dto.ResourceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaResourceMapper {

    ResourceDTO resourceToResourceDTO(JpaResource resource);

    JpaResource resourceDTOToResource(ResourceDTO resource);
}