package gr.rongasa.testing.service;

import com.querydsl.core.types.Predicate;
import gr.rongasa.testing.domain.Person;
import gr.rongasa.testing.domain.PersonDTO;
import gr.rongasa.testing.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PersonServiceImplTest {
    @Autowired
    PersonService personService;
    @MockBean
    private PersonRepository personRepository;


    private Person createPerson(Long id, String firstName, String lastName) {
        return Person.builder().firstName(firstName).lastName(lastName).id(id).build();
    }

    private PersonDTO createPersonDTO(Long id, String firstName, String lastName) {
        return PersonDTO.builder().firstName(firstName).lastName(lastName).id(id).build();
    }

    @Test
    public void findPersonByFirstName() {
        //prepare persons
        List<Person> personList = new ArrayList<>();
        personList.add(createPerson(1L, "Alex", "Rongas"));
        personList.add(createPerson(2L, "Alex", "Do"));

        PageImpl<Person> personPage = new PageImpl<>(personList, Pageable.unpaged(), personList.size());

        //mock repository
        doReturn(personPage).when(personRepository).findAll(any(Predicate.class), any(Pageable.class));

        Page<PersonDTO> page = personService.findPersonsByFirstName("Alex", Pageable.unpaged());
        assertThat(page.getTotalElements()).isEqualTo(2L);
        assertThat(page.getTotalPages()).isEqualTo(1L);
        assertThat(page.getContent()).hasSize(2);

        List<PersonDTO> personDTOList = new ArrayList<>();
        personDTOList.add(createPersonDTO(1L, "Alex", "Rongas"));
        personDTOList.add(createPersonDTO(2L, "Alex", "Do"));
        assertThat(page.getContent()).containsExactly(personDTOList.toArray(new PersonDTO[0]));
    }
}