package gr.rongasa.library.repository.jpa;

import gr.rongasa.library.domain.jpa.JpaResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

//@RestResource(exported = false)
public interface JpaResourceRepository extends JpaRepository<JpaResource, String> {
    Page<JpaResource> findByType(Pageable pageable, String trackingId);

    Page<JpaResource> findByName(Pageable pageable, String name);
}
