package gr.rongasa.testing.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import gr.rongasa.testing.domain.Person;
import gr.rongasa.testing.domain.PersonDTO;
import gr.rongasa.testing.domain.QPerson;
import gr.rongasa.testing.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Page<PersonDTO> findPersons(String firstName, String lastName, Pageable pageable) {
        BooleanExpression firstNameExpression = QPerson.person.firstName.eq(firstName);
        BooleanExpression lastNameExpression = QPerson.person.lastName.eq(lastName);
        Predicate predicate = firstNameExpression.and(lastNameExpression);
        return personRepository.findAll(predicate, pageable).map(this::mapToDTO);
    }

    @Override
    public Page<PersonDTO> findPersonsByFirstName(String firstName, Pageable pageable) {
        return personRepository.findAll(QPerson.person.firstName.eq(firstName), pageable).map(this::mapToDTO);
    }

    @Override
    public Page<PersonDTO> findAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    public PersonDTO save(PersonDTO personDTO) {
        return mapToDTO(personRepository.save(mapToEntity(personDTO)));
    }

    private Person mapToEntity(PersonDTO person) {
        return Person.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .id(person.getId()).build();
    }

    private PersonDTO mapToDTO(Person person) {
        return PersonDTO.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .id(person.getId()).build();
    }
}
