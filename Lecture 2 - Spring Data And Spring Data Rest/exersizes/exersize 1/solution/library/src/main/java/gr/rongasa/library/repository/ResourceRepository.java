package gr.rongasa.library.repository;

import gr.rongasa.library.domain.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ResourceRepository extends ElasticsearchRepository<Resource, String> {
    Page<Resource> findByType(Pageable pageable, String trackingId);

    Page<Resource> findByName(Pageable pageable, String name);
}
