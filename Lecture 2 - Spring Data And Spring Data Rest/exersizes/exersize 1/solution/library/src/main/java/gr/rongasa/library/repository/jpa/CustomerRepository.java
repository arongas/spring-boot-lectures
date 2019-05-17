package gr.rongasa.library.repository.jpa;

import gr.rongasa.library.domain.jpa.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,String> {
    Optional<Customer> findById(Pageable pageable, @Param("id") Long id);

    Page<Customer> findByFirstName(Pageable pageable, @Param("type") String type);

    Page<Customer> findByLastName(Pageable pageable, @Param("name") String name);

    Page<Customer> findByBorrowedMediasName(Pageable pageable, @Param("author") String author);

}
