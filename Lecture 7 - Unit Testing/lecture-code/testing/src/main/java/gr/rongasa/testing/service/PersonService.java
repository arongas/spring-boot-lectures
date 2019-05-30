package gr.rongasa.testing.service;

import gr.rongasa.testing.domain.Person;
import gr.rongasa.testing.domain.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {
    Page<PersonDTO> findPersons(String firstName, String lastName, Pageable pageable);
    Page<PersonDTO> findPersonsByFirstName(String firstName, Pageable pageable);
    Page<PersonDTO> findAllPersons(Pageable pageable);

    PersonDTO save(PersonDTO personDTO);
}
