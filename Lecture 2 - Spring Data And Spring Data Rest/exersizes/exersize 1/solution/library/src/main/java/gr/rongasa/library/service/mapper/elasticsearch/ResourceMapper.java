package gr.rongasa.library.service.mapper.elasticsearch;

import gr.rongasa.library.domain.elasticsearch.Resource;
import gr.rongasa.library.domain.dto.ResourceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    ResourceDTO resourceToResourceDTO(Resource resource);

    Resource resourceDTOToResource(ResourceDTO resource);
}