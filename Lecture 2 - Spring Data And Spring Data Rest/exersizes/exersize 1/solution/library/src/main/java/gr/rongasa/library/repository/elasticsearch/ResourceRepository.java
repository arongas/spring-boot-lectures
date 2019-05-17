package gr.rongasa.library.repository.elasticsearch;

import gr.rongasa.library.domain.elasticsearch.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceRepository extends ElasticsearchRepository<Resource, String> {
//    Optional<Resource> findByTrackingId(Pageable pageable, @Param("id") String id);

//    Page<Resource> findByType(Pageable pageable, @Param("type") String type);
//
//    Page<Resource> findByName(Pageable pageable, @Param("name") String name);
//
//    Page<Resource> findByAuthor(Pageable pageable, @Param("author") String author);
//
//    Page<Resource> findByDescriptionContaining(Pageable pageable, @Param("name") String name);
//
//    Page<Resource> findByNameContainingAndAuthor(Pageable pageable, @Param("name") String name, @Param("author") String author);

}
