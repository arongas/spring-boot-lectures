package gr.rongasa.library.repository.elasticsearch;

import gr.rongasa.library.domain.elasticsearch.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ResourceRepository extends ElasticsearchRepository<Resource, String> {
    Page<Resource> findByType(Pageable pageable, String trackingId);

    Page<Resource> findByName(Pageable pageable, String name);

//    @Override
//    <S extends Resource> S save(S entity);
//
//    @Override
//    <S extends Resource> Iterable<S> saveAll(Iterable<S> entities);
//
//    @RestResource(exported = false)
//    @Override
//    void deleteById(String s);
//
//    @RestResource(exported = false)
//    @Override
//    void delete(Resource entity);
//
//    @RestResource(exported = false)
//    @Override
//    void deleteAll(Iterable<? extends Resource> entities);
//
//    @RestResource(exported = false)
//    @Override
//    void deleteAll();
}
