package gr.rongasa.library.service.mapper;

import gr.rongasa.library.domain.Resource;
import gr.rongasa.library.web.dto.ResourceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    ResourceDTO resourceToResourceDTO(Resource resource);

    Resource resourceDTOToResource(ResourceDTO resource);
}