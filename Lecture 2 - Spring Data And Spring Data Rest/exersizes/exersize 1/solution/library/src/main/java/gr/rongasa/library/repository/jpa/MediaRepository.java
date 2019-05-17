package gr.rongasa.library.repository.jpa;

import gr.rongasa.library.domain.jpa.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media,String> {

    Page<Media> findByCustomerIsNull(Pageable pageable);
    Page<Media> findByCustomerIsNotNull(Pageable pageable);
    Page<Media> findByCustomerFirstNameAndCustomerLastName(Pageable pageable, String firstName, String lastName);

}
